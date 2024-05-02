-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- Full_Shifter.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains Full shifter
--
--
-- NOTES:
-- 3/6/23 :: Design created.
-------------------------------------------------------------------------


library IEEE;
use IEEE.std_logic_1164.all;
entity Full_Shifter is
generic( N :integer:=32);


port(i_input       : in std_logic_vector(31 downto 0);
     i_Shift_amout : std_logic_vector(4 downto 0);
     i_Logical_ctl : std_logic;
     i_Shift_right  : std_logic;	-- if 1 then right shift
     o_shift_out   : out std_logic_vector (31 downto 0));
	
end Full_Shifter;

architecture structure of Full_Shifter is


component Full_Barrel_Left
port(i_input       : in std_logic_vector(31 downto 0);
     i_Shift_amout : std_logic_vector(4 downto 0);
     i_Logical_ctl : std_logic;
     o_shift_out   : out std_logic_vector (31 downto 0));
	

end component;

component Full_Barrel_Right
port(i_input       : in std_logic_vector(31 downto 0);
     i_Shift_amout : std_logic_vector(4 downto 0);
     i_Logical_ctl : std_logic;
     o_shift_out   : out std_logic_vector (31 downto 0));
	
end component;


component mux2_1Nbit
  port(i_A  : in std_logic_vector(31 downto 0);
       i_B  : in std_logic_vector(31 downto 0);
       i_S  : in std_logic;
       o_F  : out std_logic_vector(31 downto 0));
end component;

signal R_OUT, L_OUT : std_logic_vector (31 downto 0);



begin


Full_Right : Full_Barrel_Right
port map(i_input => i_input,
         i_Shift_amout => i_Shift_amout,
	 i_Logical_ctl => i_Logical_ctl,
	 o_shift_out => R_OUT);
		 
Full_Left : Full_Barrel_Left
port map(i_input => i_input,
         i_Shift_amout => i_Shift_amout,
         i_Logical_ctl => '0',
	 o_shift_out => L_OUT);

Dat_Mux : mux2_1Nbit
port map(i_A => L_OUT,
	 i_B => R_OUT,
	 i_S => i_Shift_right,
	 o_F => o_shift_out);
		

end structure;