addi $t1, $0, 2
first:
sub $t1, $t1, 1
beq $t1, $t2, third
second:
bne $t1, $t2, first
third:
addi $t1, $0, -1
addi $t2, $0, -2
j fifth
fourth:
jal sixth
fifth:
jal fifthAgain
fifthAgain:
beq $t1, $t2, fourth
addi $t2, $t2, 1
jr $ra
sixth:
addi $t1, $0, 1
addi $t2, $0, -2
bgez $t2, eighth # Doesn't matter, shouldnt branch
seventh:
bgezal $t1, ninth
eighth:
addi $t2, $t2, 3
bgtz $t2, tenth
ninth:
blez $t2, eighth
tenth:
j dummy
#bltzal $t2, dummy
eleventh:
sub $t1, $t1, $t2
# bltz $t1, exit
j exit
j eleventh

dummy:
addi $t1, $0, 5
addi $t2, $0, 1
j eleventh

exit:
halt
