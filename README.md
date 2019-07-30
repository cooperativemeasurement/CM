# cooperative measurement(CM)


# How to compile and run

compile: javac main.java

run: java main trace-path FG cells-num
(trace-path is the path of the trace, the trace should be a .txt file),
(if FG =0 the trace will run on the FatTree topology otherwise will run in Geant)
(cells-num the number of entries in each switch).

#  The results

after running the program a new text file named "result.txt" will be created and will contain the results. 
