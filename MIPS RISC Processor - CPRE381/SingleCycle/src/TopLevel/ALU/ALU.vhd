-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- ALU.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains ALU
--
--
-- NOTES:
-- 3/9/23 :: Design created.


-- Need to use OC in adder, need to check unsigned
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity ALU is
generic( N :integer:=32);
  port(i_aALU	  : in std_logic_vector(31 downto 0);
       i_bALU	  : in std_logic_vector(31 downto 0);
       i_Scontrol : in std_logic_vector(5 downto 0);
       i_shamt    : in std_logic_vector(4 downto 0);
       o_OUT	  : out std_logic_vector(31 downto 0);
       o_C	  : out std_logic;
       o_Over     : out std_logic;
       o_Zero     : out std_logic);
end ALU;

architecture arch_ALU of ALU is

component Add_Sub_N is
  port(i_X	: in std_logic_vector(31 downto 0);
       i_Y	: in std_logic_vector(31 downto 0);
       nAdd_Sub	: in std_logic; -- 1 for sub, 0 for add
       o_C	: out std_logic;
       o_Overflow : out std_logic;
       o_S	: out std_logic_vector(31 downto 0));
end component;

component Full_Shifter is
  port(i_input	        : in std_logic_vector(31 downto 0);
       i_Shift_amout	: in std_logic_vector(4 downto 0);
       i_Logical_ctl	: in std_logic; -- if 1 then preserve leftmost bit (sra)
       i_Shift_right	: in std_logic; -- if 1 then shift right
       o_shift_out	: out std_logic_vector(31 downto 0));
end component;

component andg2_n is
  port(i_A	: in std_logic_vector (N-1 downto 0);
       i_B	: in std_logic_vector (N-1 downto 0);
       o_F	: out std_logic_vector (N-1 downto 0));
end component;

component andg2 is
	port(i_A	: in std_logic;
    	i_B	: in std_logic;
		o_F	: out std_logic);
end component;

component org2 is
	port(i_A	: in std_logic;
    	i_B	: in std_logic;
		o_F	: out std_logic);
end component;

component mux2_1Nbit is
  port(i_A  : in std_logic_vector(31 downto 0);
       i_B  : in std_logic_vector(31 downto 0);
       i_S  : in std_logic;
       o_F  : out std_logic_vector(31 downto 0));
end component;

component Full_OR is
  port(i_RS	: in std_logic_vector (N-1 downto 0);
       i_ALUmux	: in std_logic_vector (N-1 downto 0);
       i_S	: in std_logic_vector (1 downto 0); -- 00 for OR, 01 for XOR, 10 for NOR, 11 for nothing (x"00000000")
       o_F	: out std_logic_vector (N-1 downto 0));
end component;

component mux4_1_n is 
  port(i_D0                         : in std_logic_vector(N-1 downto 0);
       i_D1 		            : in std_logic_vector(N-1 downto 0);
       i_D2 		            : in std_logic_vector(N-1 downto 0);
       i_D3 		            : in std_logic_vector(N-1 downto 0);
       i_S			    : in std_logic_vector(1 downto 0);  -- 00 for ADD/SUB, 01 for SHIFT, 10 for AND, 11 for OR
       o_O 		            : out std_logic_vector(N-1 downto 0));
end component;

-- Signals: 
signal s_D0 : std_logic_vector (N-1 downto 0);
signal s_D1 : std_logic_vector (N-1 downto 0);
signal s_D2 : std_logic_vector (N-1 downto 0);
signal s_D3 : std_logic_vector (N-1 downto 0);
signal s_OUT : std_logic_vector (N-1 downto 0);
signal s_iA : std_logic_vector (N-1 downto 0);
signal s_iB : std_logic_vector (N-1 downto 0);

-- Logic:
signal s_MUX4 : std_logic_vector (1 downto 0);  -- control for output. 00 for ADD/SUB, 01 for SHIFT, 10 for AND, 11 for OR
signal s_ADD_SUB : std_logic; 			-- control bit 0 for ADD, 1 for SUB
signal s_ShiftL_R : std_logic; 			-- control bit 0 for left, 1 for right shift
signal s_ShiftArith : std_logic; 		-- control bit 0 for srl, 1 for sra (keep furthest left bit 1). Does nothing for left shift
signal s_sOR : std_logic_vector (1 downto 0); 	-- control bit 00 for OR, 01 for XOR, 10 for NOR, 11 for nothing (x"00000000") 
signal s_SLT : std_logic; 			-- control bit 0 for all other ops, 1 for slt/slti

signal s_shamt: std_logic_vector (4 downto 0); -- figure out how to feed instructions (10-6)
signal s_OC: std_logic; -- not sure what, if anything, this should drive
signal cond_signal : std_logic_vector(9 downto 0);

signal s_SLTout :  std_logic_vector (N-1 downto 0);

--signal check1 : std_logic_vector(9 downto 0);
--signal check2 : std_logic_vector(9 downto 0);
--signal check3 : std_logic_vector(9 downto 0);
--signal check4 : std_logic_vector(9 downto 0);

signal s_Over : std_logic;
signal s_addSub : std_logic;

signal s_zero : std_logic_vector (N-1 downto 0);
signal s_one : std_logic_vector (N-1 downto 0);
signal s_NegCheck : std_logic;
signal s_SLTSEL : std_logic;
signal s_decode : std_logic_vector (8 downto 0);
signal s_Sel : std_logic_vector (5 downto 0);
begin 

-- Generates zero bit 

o_Zero <= '1' when s_iA = s_iB else '0';

s_Sel <= i_Scontrol(5 downto 0);

with (s_Sel) select
s_decode <= "000100000" when "100000",	-- add/addi
			"000100000" when "100001",	-- addu/addiu
			"001100000" when "100010",	-- sub/subi
			"001100000" when "100011",	-- subu/subiu
			"100000000" when "100100",	-- and/andi
			"110000000" when "100101",	-- or/ori
			"110000010" when "100110",	-- xor/xori
			"110000100" when "100111",	-- nor/nori
			"010000000" when "000000",	-- sll
			"010010000" when "000010",	-- srl
			"010011000" when "000011",	-- sra
			"001100001" when "101010",	-- slt/slti
			"000000000" when others;

s_MUX4 		 <= s_decode(8 downto 7);
s_ADD_SUB	 <= s_decode(6);
s_addSub	 <= s_decode(5);
s_ShiftL_R   <= s_decode(4);
s_ShiftArith <= s_decode(3);
s_sOR        <= s_decode(2 downto 1);
s_SLT        <= s_decode(0);

-- process (s_MUX4, s_ADD_SUB, s_ShiftL_R, s_ShiftArith, s_sOR, i_Scontrol) is
-- begin
-- 	  if i_Scontrol(5 downto 0) = "100000" then -- add/addi    match with Control unit
-- 		s_MUX4       <= "00";
-- 		s_ADD_SUB    <= '0';
-- 		s_addSub     <= '1';  
-- 		s_ShiftL_R   <= '0';  -- unused for add/sub
-- 		s_ShiftArith <= '0';  -- unused for add/sub
-- 		s_sOR        <= "00"; -- unused for add/sub
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100001" then -- addu/addiu
-- 		s_MUX4       <= "00";
-- 		s_ADD_SUB    <= '0';
-- 	        s_addSub     <= '1';
-- 		s_ShiftL_R   <= '0';  -- unused for add/sub
-- 		s_ShiftArith <= '0';  -- unused for add/sub
-- 		s_sOR        <= "00"; -- unused for add/sub
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100010" then -- sub/subi
-- 		s_MUX4       <= "00";
-- 		s_ADD_SUB    <= '1';
-- 	        s_addSub     <= '1';
-- 		s_ShiftL_R   <= '0';  -- unused for add/sub
-- 		s_ShiftArith <= '0';  -- unused for add/sub
-- 		s_sOR        <= "00"; -- unused for add/sub
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100011" then -- subu/subiu
-- 		s_MUX4       <= "00";
-- 		s_ADD_SUB    <= '1';
-- 	        s_addSub     <= '1';
-- 		s_ShiftL_R   <= '0';  -- unused for add/sub
-- 		s_ShiftArith <= '0';  -- unused for add/sub
-- 		s_sOR        <= "00"; -- unused for add/sub
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100100" then -- and/andi
-- 		s_MUX4       <= "10";
-- 	        s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for and
-- 		s_ShiftL_R   <= '0';  -- unused for and
-- 		s_ShiftArith <= '0';  -- unused for and
-- 		s_sOR        <= "00"; -- unused for and
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100101" then -- or/ori
-- 		s_MUX4       <= "11";
-- 	        s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for or
-- 		s_ShiftL_R   <= '0';  -- unused for or
-- 		s_ShiftArith <= '0';  -- unused for or
-- 		s_sOR        <= "00"; 
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100110" then -- xor/xori
-- 		s_MUX4       <= "11";
-- 	    s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for xor
-- 		s_ShiftL_R   <= '0';  -- unused for xor
-- 		s_ShiftArith <= '0';  -- unused for xor
-- 		s_sOR        <= "01"; 
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "100111" then -- nor/nori
-- 		s_MUX4       <= "11";
-- 	        s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for nor
-- 		s_ShiftL_R   <= '0';  -- unused for nor
-- 		s_ShiftArith <= '0'; -- unused for nor
-- 		s_sOR        <= "10"; 
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "000000" then -- sll
-- 		s_MUX4       <= "01";
-- 	    s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for sll
-- 		s_ShiftL_R   <= '0';  
-- 		s_ShiftArith <= '0';  
-- 		s_sOR        <= "00"; -- unused for sll 
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "000010" then -- srl
-- 		s_MUX4       <= "01";
-- 	        s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for srl
-- 		s_ShiftL_R   <= '1';  
-- 		s_ShiftArith <= '0';  
-- 		s_sOR        <= "00"; -- unused for srl 
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "000011" then -- sra
-- 		s_MUX4       <= "01";
-- 	        s_addSub     <= '0';
-- 		s_ADD_SUB    <= '0';  -- unused for sra
-- 		s_ShiftL_R   <= '1';  
-- 		s_ShiftArith <= '1';  
-- 		s_sOR        <= "00"; -- unused for sra
-- 		s_SLT        <= '0';

-- 	  elsif i_Scontrol(5 downto 0) = "101010" then -- slt/slti
-- 		s_MUX4       <= "00";
-- 		s_ADD_SUB    <= '1';
-- 	        s_addSub     <= '1';
-- 		s_ShiftL_R   <= '0';  -- unused for add/sub
-- 		s_ShiftArith <= '0';  -- unused for add/sub
-- 		s_sOR        <= "00"; -- unused for add/sub
-- 		s_SLT        <= '1';

-- end if;
-- end process;

-- Generates Overflow 



--	cond_signal(5 downto 0) <= i_Scontrol(5 downto 0);
--	cond_signal(6) <= ((s_iA(31) and s_iB(31)) and (s_iA(31) xor o_OUT(31)));
--	cond_signal(7) <= (s_OC);
--	cond_signal(8) <= ((s_iA(31) xor s_iB(31)) and (s_iB(31) and o_OUT(31)));
--	cond_signal(9) <= (not s_OC);
	
--	check1 <= "0001100000" and cond_signal;
--	check2 <= "0010100001" and cond_signal;
--	check3 <= "0100100010" and cond_signal;
--	check4 <= "1000100011" and cond_signal;

--	with cond_signal select

--		o_Over <= '1' when check1,
--			  '1' when check2,
--			  '1' when check3,
--			  '1' when check4,
--			  '0' when others;

s_iA <= i_aALU; -- set input signal to aALU
s_iB <= i_bALU; -- set input signal to aALU
s_shamt <= i_shamt; -- set shift ammount

G_MUXout: mux4_1_n port map(
	i_D0	 =>	s_D0,
	i_D1	 =>	s_D1,
	i_D2	 =>	s_D2,
	i_D3	 =>	s_D3,
	i_S	 =>	s_MUX4,
	o_O	 =>	s_OUT);

s_zero  <= x"00000000";
s_one   <= x"00000001";

-- G_signCheck: andg2 port map(
-- 	i_A		=> s_iA(31),
-- 	i_B		=> s_iB(31),
-- 	O_F		=> s_NegCheck);

-- G_orSLT: org2 port map(
-- 	i_A		=> s_OUT(31),
-- 	i_B		=> s_NegCheck,
-- 	o_F		=> s_SLTSEL);

s_SLTSEL <= s_OUT(31);



G_MUXslt: mux2_1Nbit port map(
	i_A      =>     s_zero,
	i_B      =>     s_one,
	i_S      =>     s_SLTSEL,
	o_F      =>     s_SLTout);

G_MUXfinal: mux2_1Nbit port map(
	i_A      =>     s_OUT,
	i_B      =>     s_SLTout,
	i_S      =>     s_SLT,
	o_F      =>     o_OUT);




-- s_OUT <= o_OUT;

G_ADD_SUB: Add_Sub_N port map( -- 0
	i_X	 =>	s_iA,
	i_Y	 =>	s_iB,
	nAdd_Sub =>	s_ADD_SUB, -- control bit 0 for ADD, 1 for SUB
	o_C	 =>	s_OC,
        o_Overflow =>   s_Over,      
	o_S	 =>	s_D0);

o_C <= s_OC;
o_Over <= s_Over and s_addSub;

G_SHIFT: Full_Shifter port map( -- 1
	i_input	       => s_iB, -- changed
	i_Shift_amout  => s_shamt, -- need to figure out how to get shamt
	i_Logical_ctl  => s_ShiftArith,-- control bit 0 for srl, 1 for sra (keep furthest left bit 1). Does nothing for left shift
	i_Shift_right  => s_ShiftL_R, -- control bit 0 for left shift, 1 for right shift
	o_shift_out    => s_D1);

-- Overflow. Something isn't right
G_AND: andg2_n port map( -- 2
	i_A	 =>	s_iA,
	i_B	 =>	s_iB,
	o_F	 =>	s_D2);

G_OR: Full_OR port map( -- 3
	i_RS	 =>	s_iA,
	i_ALUmux =>	s_iB,
	i_S	 =>	s_sOR,
	o_F	 =>	s_D3);

end arch_ALU;
