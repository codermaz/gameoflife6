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
		lives
			2<= neighbours <=3
		dies
			<2 neighbour
			>3 neighbours
	dead
		lives
			=3 neighbours

write board (next generation)

