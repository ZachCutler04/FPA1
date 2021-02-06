## Usage

Clojure Sudoko solver. Solving is done randomly, meaning you are not guaranteed to get the same solution on the board twice if you solve it twice. 

Theres some comments on functions in the code, but ill briefly describe key functionality here. 

Generating new boards is done by Solving an empty board (randomly) then replacing certain amounts with zeros. Randomness of 1 creates normal looking boards, with harder > 1 and easier < 1

generate(width height randomness) 

Solving empty boards can be done with 

main(width height)

Solving given boards can be done with

solveBoard(width height board) 

where "board" is a 1d vector representing the board. 
