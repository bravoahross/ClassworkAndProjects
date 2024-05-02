---------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- mux4_1.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 4 bit mux
--
--
-- NOTES:
-- 3/6/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux4_1 is
	Port (  i_A : in std_logic;
		i_B : in std_logic;
		i_C : in std_logic;
		i_D : in std_logic;
		i_S : in std_logic_vector (1 downto 0);
		o_F : out std_logic);
end mux4_1;

architecture arch_mux4 of mux4_1 is 



begin 



o_F <= i_A when i_S = "00" else 
	   i_B when i_S = "01" else 
	   i_C when i_S = "10" else 
	   i_D when i_S = "11" else i_D;

end arch_mux4;
