-------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------
-- control.vhd
--------------
-- DESCRIPTION: This file contains the control unit and ALU control for project 1
--
--
-- NOTES:
-- 3/2/23 :: Design created
----------------------------

LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;

ENTITY control IS
	PORT (
		i_Instruction : IN STD_LOGIC_VECTOR (31 DOWNTO 0);
		o_RegDst : out std_logic_vector(1 downto 0);
		o_RegWrDataSel : out std_logic_vector(1 downto 0);
		o_Jump : out std_logic;
		o_Branch : out std_logic;
		o_BranchSel : out std_logic_vector(2 downto 0);
		o_JR : out std_logic;
		o_JAL : out std_logic;
		o_MemRead : out std_logic;
		o_MemtoReg : OUT STD_LOGIC;
		o_MemWrite : out std_logic;
		o_ALUSrc : OUT STD_LOGIC;
		o_RegWrite : OUT STD_LOGIC;
		o_RegWriteSel : OUT STD_LOGIC;
		o_Halt :OUT STD_LOGIC;
		o_EXT  : OUT STD_LOGIC;
		o_ALUOp : OUT STD_LOGIC_VECTOR (5 DOWNTO 0));
END control;

ARCHITECTURE arch_control OF control IS

signal s_Sel1   : std_logic_vector (5 downto 0);
signal s_Sel2   : std_logic_vector (5 downto 0);
signal s_Sel    : std_logic_vector (11 downto 0);
signal s_decode : std_logic_vector (24 downto 0);




BEGIN

s_Sel1 <= i_Instruction(31 downto 26);
s_Sel2 <= i_Instruction(5 downto 0);
s_Sel  <= s_Sel1 & s_Sel2;

-- need to use don't care bits

s_decode <= "0000000000000011011000000" when s_Sel1 = "001000" else -- addi
            "0000000000000000000000001" when s_Sel1 = "010100" else -- HALT
            "0000000000000011011000010" when s_Sel1 = "001001" else -- addiu
            "0000000000000011001001000" when s_Sel1 = "001100" else -- andi
            "0001000000000011000000000" when s_Sel1 = "001111" else -- lui
            "0000000000011011001000000" when s_Sel1 = "100011" else -- lw
            "0000000000000011011010100" when s_Sel1 = "001010" else -- slti
            "0000000000000110001000000" when s_Sel1 = "101011" else -- sw
            "0000010000000000000000000" when s_Sel1 = "000100" else -- beq
            "0000010010000000000000000" when s_Sel1 = "000101" else -- bne
            "0000100000000000000000000" when s_Sel1 = "000010" else -- jump
            "1111100000100001000000000" when s_Sel1 = "000011" else -- jal
            "0000000000000011001001100" when s_Sel1 = "001110" else -- xori
            "0000000000000011001001010" when s_Sel1 = "001101" else -- ori
            "0000010100000000001010100" when s_Sel1 = "000001" else -- bgez
            "1100010100000001101010100" when s_Sel1 = "010000" else -- bgezal
            "0000010110000000001010100" when s_Sel1 = "000111" else -- bgtz
            "0000011000000000001010100" when s_Sel1 = "000110" else -- blez
            "1100011010000001101010100" when s_Sel1 = "010010" else -- bltzal
            "1100011010000010001010100" when s_Sel1 = "010011" else -- bltz
            "0100000000000001001000010" when s_Sel  = "000000100001" else -- addu
            "0100000000000001011000100" when s_Sel  = "000000100010" else -- sub
			"0100000000000001001000110" when s_Sel  = "000000100011" else -- subu
            "0100000000000001001001000" when s_Sel  = "000000100100" else -- and
            "0100000000000001001001010" when s_Sel  = "000000100101" else -- or
            "0100000000000001001001100" when s_Sel  = "000000100110" else -- xor
            "0100000000000001001001110" when s_Sel  = "000000100111" else -- nor
            "0100000000000001001010100" when s_Sel  = "000000101010" else -- slt
            "0100000000000001001000000" when s_Sel  = "000000100000" else -- add
            "0100000000000001000000000" when s_Sel  = "000000000000" else -- sll
            "0100000000000001000000100" when s_Sel  = "000000000010" else -- srl
            "0100000000000001000000110" when s_Sel  = "000000000011" else -- sra
			"0100000000000000000000000" when s_Sel  = "000000001100" else -- syscall
            "0000000001000000000000000" when s_Sel  = "000000001000" else "XXXXXXXXXXXXXXXXXXXXXXXXX"; -- jr and others case


o_RegDst 		<= s_decode(24 downto 23);
o_RegWrDataSel  <= s_decode(22 downto 21);
o_Jump 			<= s_decode(20);
o_Branch		<= s_decode(19);
o_BranchSel 	<= s_decode(18 downto 16);
o_JR 			<= s_decode(15);
o_JAL 			<= s_decode(14);
o_MemRead 		<= s_decode(13);
o_MemtoReg 		<= s_decode(12);
o_MemWrite 		<= s_decode(11);
o_ALUSrc		<= s_decode(10);
o_RegWrite		<= s_decode(9);
o_RegWriteSel 	<= s_decode(8);
o_EXT 			<= s_decode(7);
o_ALUOp 		<= s_decode(6 downto 1);
o_Halt			<= s_decode(0);


	-- PROCESS (i_Instruction)
	-- BEGIN

		-- IF i_Instruction(31 DOWNTO 26) = "001000" THEN -- addi
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1'; -- Immediate
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '1';
		-- 	o_ALUOp <= "100000"; -- This is Add in ALU

		-- ELSIF i_Instruction(31 DOWNTO 26) = "010100" THEN -- HALT
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '1';
		-- 	o_ALUOp <= "100001"; -- Add unsigned
		-- 	o_Halt <= '1';

		-- ELSIF i_Instruction(31 DOWNTO 26) = "001001" THEN -- addiu
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '1';
		-- 	o_ALUOp <= "100001"; -- Add unsigned

		-- ELSIF i_Instruction(31 DOWNTO 26) = "001100" THEN -- andi
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100100"; -- CHANGE THIS LATER

		-- ELSIF i_Instruction(31 DOWNTO 26) = "001111" THEN -- lui
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "01";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000";

		-- ELSIF i_Instruction(31 DOWNTO 26) = "100011" THEN -- lw
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '1';
		-- 	o_MemtoReg <= '1';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100000";

		-- ELSIF i_Instruction(31 DOWNTO 26) = "001010" THEN -- slti
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '1';
		-- 	o_ALUOp <= "101010"; -- This is slt in ALU

		-- ELSIF i_Instruction(31 DOWNTO 26) = "101011" THEN -- sw
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '1';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100000";

		-- ELSIF i_Instruction(31 DOWNTO 26) = "000100" THEN -- beq
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000"; -- Need to check out again

		-- ELSIF i_Instruction(31 DOWNTO 26) = "000101" THEN -- bne
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "001";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000"; -- Reevaluate

		-- ELSIF i_Instruction(31 DOWNTO 26) = "000010" THEN -- j
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '1';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000";

		-- ELSIF i_Instruction(31 DOWNTO 26) = "000011" THEN -- jal
		-- 	o_RegDst <= "11";
		-- 	o_RegWrDataSel <= "11";
		-- 	o_Jump <= '1';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '1';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "001110" THEN -- xori
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100110";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "001101" THEN -- ori
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100101";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "000001" THEN -- bgez
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "010";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "010000" THEN -- bgezal
		-- 	o_RegDst <= "11";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "010";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '1';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "000111" THEN -- bgtz
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "011";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "000110" THEN -- blez
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "100";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "010010" THEN -- bltzal
		-- 	o_RegDst <= "11";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "101";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '1';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF i_Instruction(31 DOWNTO 26) = "010011" THEN -- bltz
		-- 	o_RegDst <= "11";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '1';
		-- 	o_BranchSel <= "101";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '1'; 
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	-- R type instructions. ALU control cases. If i_Instructions(31-26) = 000000


		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100001") THEN -- addu 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100001";


		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100010") THEN -- sub
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '1';
		-- 	o_ALUOp <= "100010";

		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100011") THEN -- subu
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100011";

		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100100") THEN -- and
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100100";

		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100101") THEN -- or
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100101";

		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100110") THEN -- xor
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100110";

		-- ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100111") THEN -- nor
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100111";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "101010") THEN -- slt 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "101010";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "100000") THEN -- add 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "100000";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "000000") THEN -- sll 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "000010") THEN -- srl 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000010";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "000011") THEN -- sra 
		-- 	o_RegDst <= "01";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0';
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '0';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '1';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000011";

		-- 	ELSIF (i_Instruction(31 DOWNTO 26) = "000000") and (i_Instruction(5 DOWNTO 0) = "001000") THEN -- jr 
		-- 	o_RegDst <= "00";
		-- 	o_RegWrDataSel <= "00";
		-- 	o_Jump <= '0'; -- changed from 1
		-- 	o_Branch <= '0';
		-- 	o_BranchSel <= "000";
		-- 	o_JR <= '1';
		-- 	o_JAL <= '0';
		-- 	o_MemRead <= '0';
		-- 	o_MemtoReg <= '0';
		-- 	o_MemWrite <= '0';
		-- 	o_ALUSrc <= '0';
		-- 	o_RegWrite <= '0';
		-- 	o_RegWriteSel <= '0';
		-- 	o_EXT <= '0';
		-- 	o_ALUOp <= "000000";

		-- else for unrecognized code? 

		-- END IF;

	-- END PROCESS;
END arch_control;