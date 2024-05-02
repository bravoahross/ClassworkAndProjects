-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- control.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains the control unit and ALU control for project 1
--
--
-- NOTES:
-- 3/2/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity R_Barrell_shift is
generic( N :integer:=32);
port(    mux_in : in std_logic_vector (63 downto 0);
	 mux_out : out std_logic_vector (31 downto 0);
	 mux_select : in std_logic);
	
end R_Barrell_shift;

architecture structure of R_Barrell_shift is

component mux2
  port(i_A  : in std_logic;
       i_B  : in std_logic;
       i_X  : in std_logic;
       o_F  : out std_logic);
end component;



--signal

begin

G1: for i in 0 to (N-1) generate


Mux1 : mux2
port map(i_B  =>mux_in(i*2), -- a is picked when i_X is 0
         i_A  =>mux_in(i*2+1),  -- b is picked by a mux select of 1
         i_X  =>mux_select,
         o_F  => mux_out(i));
		 
end generate;

end structure;