package de.codermaz.coderetreat.gameoflife.guifx.saveboard;

import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeModel;
import de.codermaz.coderetreat.gameoflife.model.BoardInfo;
import de.codermaz.coderetreat.gameoflife.model.BoardInfoXml;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.io.File;
import java.util.Deque;
import java.util.Objects;

import static de.codermaz.coderetreat.gameoflife.App.TITLE_GAME_OF_LIFE;


public class SaveBoardPresenter
{
	@Inject
	GameOfLifeModel gameOfLifeModel;
	@Inject
	SaveBoardModel  saveBoardModel;

	@FXML
	VBox      saveBoardVBox;
	@FXML
	Button    saveAsButton;
	@FXML
	Button    returnButton;
	@FXML
	TextField boardNameTextField;
	@FXML
	TextField rowCountTextField;
	@FXML
	TextField colCountTextField;

	private StringProperty              boardNameText  = new SimpleStringProperty();
	private SimpleObjectProperty<File>  lastChosenFile = new SimpleObjectProperty();
	private SimpleObjectProperty<Stage> primaryStage;

	@FXML
	void initialize()
	{
		primaryStage = gameOfLifeModel.primaryStageProperty();
		lastChosenFile = saveBoardModel.lastChosenFileProperty();

		prepareBoardNameTextField();
		BooleanBinding boardNameNotEntered = boardNameText.isEmpty();
		saveAsButton.disableProperty().bind( boardNameNotEntered );
		prepareContentOfTextFields();

		handleMenuButtons();
		requestFocusFor( boardNameTextField );
	}

	private void prepareBoardNameTextField()
	{
		boardNameText = boardNameTextField.textProperty();
		boardNameTextField.setOnKeyPressed( (event) -> {
			if( event.getCode() == KeyCode.ENTER )
			{
				FileChooser fileChooser = prepareFileChooser();
				saveAsButtonPressed( fileChooser );
			}
		} );
	}

	private void prepareContentOfTextFields()
	{
		rowCountTextField.textProperty().set( String.valueOf( gameOfLifeModel.gameBoardProperty().get().length ) );
		colCountTextField.textProperty().set( String.valueOf( gameOfLifeModel.gameBoardProperty().get()[0].length ) );
	}

	private void requestFocusFor(Node node)
	{   // without runLater it does not run correct
		Platform.runLater( () -> {
			if( !node.isFocused() )
			{
				node.requestFocus();
				requestFocusFor( node );
			}
		} );
	}

	private void handleMenuButtons()
	{
		FileChooser fileChooser = prepareFileChooser();
		saveAsButton.setOnAction( event -> saveAsButtonPressed( fileChooser ) );
		returnButton.setOnAction( this::returnButtonPressed );
	}

	private void returnButtonPressed(ActionEvent actionEvent)
	{
		primaryStage.get().setScene( gameOfLifeModel.returnSceneProperty().get() );
		primaryStage.get().setTitle( TITLE_GAME_OF_LIFE + " - " + boardNameText.get() + " - " + lastChosenFile.get().getAbsolutePath() );
		primaryStage.get().show();
	}

	private FileChooser prepareFileChooser()
	{
		FileChooser fileChooser = new FileChooser();
		configuringFileChooser( fileChooser );
		return fileChooser;
	}

	private void saveAsButtonPressed(FileChooser fileChooser)
	{
		File selectedFile = getSelectedFileToSave( fileChooser );
		if( Objects.nonNull( selectedFile ) )
		{
			BoardInfo boardInfo = new BoardInfo();
			String fullPathOfXmlFile = prepareFileForMarshalling( selectedFile, boardInfo );
			if( fullPathOfXmlFile == null )
				return;
			if( !readBoardProperties( boardInfo, fullPathOfXmlFile ) )
				return;

			updateSaveBoardModel( selectedFile, fullPathOfXmlFile );
			updateOpenRecentSubMenu();
			primaryStage.get().setTitle( TITLE_GAME_OF_LIFE + " - " + boardNameText.get() + " saved" );
		}
	}

	private void updateOpenRecentSubMenu()
	{
		Deque<File> recentOpenedFiles = saveBoardModel.getRecentOpenedFiles();
		gameOfLifeModel.prepareSubMenu( recentOpenedFiles );
	}

	private File getSelectedFileToSave(FileChooser fileChooser)
	{
		File lastChosenDirectory = new File( saveBoardModel.lastChosenFileProperty().get().getParent() );
		fileChooser.setInitialDirectory( lastChosenDirectory );
		fileChooser.setInitialFileName( boardNameText.get() );
		return fileChooser.showSaveDialog( gameOfLifeModel.primaryStageProperty().get() );
	}

	private void updateSaveBoardModel(File selectedFile, String fullPathOfXmlFile)
	{
		saveBoardModel.lastChosenFileProperty().set( new File( fullPathOfXmlFile ) );
		saveBoardModel.addToRecentFiles( selectedFile );
	}

	private boolean readBoardProperties(BoardInfo boardInfo, String fullPathOfXmlFile)
	{
		int[][] board = gameOfLifeModel.gameBoardProperty().get();
		BoardInfoXml boardInfoXml = boardInfo.gameBoardToBoardInfoXml( board );
		boardInfoXml.setName( boardNameText.get() );
		if( !boardInfo.jaxbObjectToXML( boardInfoXml, fullPathOfXmlFile ) )
		{
			System.out.println( "Board info could not be written to " + fullPathOfXmlFile );
			return false;
		}
		return true;
	}

	private String prepareFileForMarshalling(File selectedFile, BoardInfo boardInfo)
	{
		String fullPathOfXmlFile = selectedFile.getAbsolutePath();
		if( !boardInfo.prepareFileForMarshalling( fullPathOfXmlFile ) )
		{
			System.out.println( fullPathOfXmlFile + " could not prepared for marshalling!" );
			return null;
		}
		return fullPathOfXmlFile;
	}

	private void configuringFileChooser(FileChooser fileChooser)
	{
		fileChooser.setTitle( "Save board xml file" );

		fileChooser.getExtensionFilters().addAll(//
			//			new FileChooser.ExtensionFilter( "All Files", "*.*" ), //
			new FileChooser.ExtensionFilter( "XML", "*.xml" ) );
	}
}
