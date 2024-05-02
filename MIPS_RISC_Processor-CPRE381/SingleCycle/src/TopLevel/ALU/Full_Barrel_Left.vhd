-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- Full_Barrel_Left.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains the left shifter
--
--
-- NOTES:
-- 3/2/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity Full_Barrel_Left is
generic( N :integer:=32);
port(i_input       : in std_logic_vector(31 downto 0);
     i_Shift_amout : std_logic_vector(4 downto 0);
     i_Logical_ctl : std_logic;
     o_shift_out   : out std_logic_vector (31 downto 0));
	
end Full_Barrel_Left;

architecture structure of Full_Barrel_Left is

component R_Barrell_shift
port(    mux_in : in std_logic_vector (63 downto 0);
	 mux_out : out std_logic_vector (31 downto 0);
	 mux_select : in std_logic);
end component;

component mux2
  port(i_A  : in std_logic;
       i_B  : in std_logic;
       i_X  : in std_logic;
       o_F  : out std_logic);
end component;

signal arth_logical_bit : std_logic;
signal mux0_in,mux1_in,mux2_in,mux3_in,mux4_in : std_logic_vector(63 downto 0);
signal mux0, mux1, s_mux2, mux3, mux4 : std_logic_vector(31 downto 0);

--signal

begin

mux0_in(0) <=i_input(0);-- mux level 0
mux0_in(1) <=arth_logical_bit;

mux1_in(0) <= mux0(0);  --mux level 1
mux1_in(1) <= arth_logical_bit;
mux1_in(2) <= mux0(1);
mux1_in(3) <= arth_logical_bit;


mux2_in(0) <=mux1(0);--mux level 2
mux2_in(1) <=arth_logical_bit;
mux2_in(2) <=mux1(1);
mux2_in(3) <=arth_logical_bit;
mux2_in(4) <=mux1(2);
mux2_in(5) <=arth_logical_bit;
mux2_in(6) <=mux1(3);
mux2_in(7) <=arth_logical_bit;


mux3_in(0) <=s_mux2(0);
mux3_in(1) <=arth_logical_bit;
mux3_in(2) <=s_mux2(1);
mux3_in(3) <=arth_logical_bit;
mux3_in(4) <=s_mux2(2);
mux3_in(5) <=arth_logical_bit;
mux3_in(6) <=s_mux2(3);
mux3_in(7) <=arth_logical_bit;
mux3_in(8) <=s_mux2(4);
mux3_in(9) <=arth_logical_bit;
mux3_in(10) <=s_mux2(5);
mux3_in(11) <=arth_logical_bit;
mux3_in(12) <=s_mux2(6);
mux3_in(13) <=arth_logical_bit;
mux3_in(14) <=s_mux2(7);
mux3_in(15) <=arth_logical_bit;


mux4_in(0) <=mux3(0);
mux4_in(1) <=arth_logical_bit;
mux4_in(2) <=mux3(1);
mux4_in(3) <=arth_logical_bit;
mux4_in(4) <=mux3(2);
mux4_in(5) <=arth_logical_bit;
mux4_in(6) <=mux3(3);
mux4_in(7) <=arth_logical_bit;
mux4_in(8) <=mux3(4);
mux4_in(9) <=arth_logical_bit;
mux4_in(10) <=mux3(5);
mux4_in(11) <=arth_logical_bit;
mux4_in(12) <=mux3(6);
mux4_in(13) <=arth_logical_bit;
mux4_in(14) <=mux3(7);
mux4_in(15) <=arth_logical_bit;
mux4_in(16) <=mux3(8);
mux4_in(17) <=arth_logical_bit;
mux4_in(18) <=mux3(9);
mux4_in(19) <=arth_logical_bit;
mux4_in(20) <=mux3(10);
mux4_in(21) <=arth_logical_bit;
mux4_in(22) <=mux3(11);
mux4_in(23) <=arth_logical_bit;
mux4_in(24) <=mux3(12);
mux4_in(25) <=arth_logical_bit;
mux4_in(26) <=mux3(13);
mux4_in(27) <=arth_logical_bit;
mux4_in(28) <=mux3(14);
mux4_in(29) <=arth_logical_bit;
mux4_in(30) <=mux3(15);
mux4_in(31) <=arth_logical_bit;




G0: for i in 1 to (31) generate
mux0_in(i*2) <=i_input(i); --mux 0 sel space
mux0_in(i*2+1) <=i_input(i-1); -- mux 1 sel space

end generate;

G1: for i in 2 to (31) generate
mux1_in(i*2+1) <= mux0(i-2);
mux1_in(i*2) <= mux0(i);

end generate;

G2: for i in 4 to (31) generate
mux2_in(i*2+1) <= mux1(i-4);
mux2_in(i*2) <= mux1(i);

end generate;

G3: for i in 8 to (31) generate
mux3_in(i*2+1) <= s_mux2(i-8);
mux3_in(i*2) <= s_mux2(i);

end generate;

G4: for i in 16 to (31) generate
mux4_in(i*2+1) <= mux3(i-16);
mux4_in(i*2) <= mux3(i);

end generate;










Arth_Logi : mux2
port map(i_B  =>'0',
         i_A  =>i_input(31),
         i_X  =>i_Logical_ctl,
         o_F  => arth_logical_bit);
		 



shift0 : R_Barrell_shift
port map(mux_in =>mux0_in,
	     mux_out =>mux0,
	     mux_select =>i_Shift_amout(0));

shift1 : R_Barrell_shift
port map(mux_in =>mux1_in,
	     mux_out =>mux1,
	     mux_select =>i_Shift_amout(1));

shift2 : R_Barrell_shift
port map(mux_in =>mux2_in,
	     mux_out =>s_mux2,
	     mux_select =>i_Shift_amout(2));

shift3 : R_Barrell_shift
port map(mux_in =>mux3_in,
	     mux_out =>mux3,
	     mux_select =>i_Shift_amout(3));

shift4 : R_Barrell_shift
port map(mux_in =>mux4_in,
	     mux_out =>o_shift_out,
	     mux_select =>i_Shift_amout(4));





end structure;