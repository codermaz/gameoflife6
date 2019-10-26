The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970

Any live cell with fewer than two live neighbours dies, as if caused by under-population.

Any live cell with two or three live neighbours lives on to the next generation.

Any live cell with more than three live neighbours dies, as if by overcrowding.

Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
--------------------------

read intial board

klick on next >

cell:
	alive
		neighbours
		    2 or 3
		        lives
		    <2 or >3
		        dies
	dead
		neighbours
		    =3
		        lives
//
cell:
    neighbours
        <2 or >3
            dies
        =2 and isAlive
            lives
        =3
            lives

write board (next generation)

