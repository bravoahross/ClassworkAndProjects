
addi $t3, $0, 08210
addiu $t2, $0, 248
add $t1, $t2, $t3
addu $t1, $t1, $0
and $t4, $t3, $t1
andi $t4, $t3, 08210
lui $t5, 0x1001
nor $t4, $t3, $t1
xor $t4, $t3, $t1
xori $t1, $t2, 100
or $t4, $t3, $t1
ori $t1, $t2, 100
slt $t1, $t2, $t3
slti $t1, $t2, -100
sll $t1, $t2, 10
srl $t1, $t2, 10
sra $t1, $t2, 10
lui $t5, 0x1001
sw $t1, 0($t5)
lw $t6, 0($t5)
sub $t1, $t2, $t3
subu $t1, $t2, $t3
halt
