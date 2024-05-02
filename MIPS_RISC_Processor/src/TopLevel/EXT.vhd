-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- EXT.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 16 to 32 bit SIGNED extender
--
--
-- NOTES:
-- 2/18/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity EXT is
    Port ( Sel : in  STD_LOGIC;
	   i_EXT_16 : in  STD_LOGIC_VECTOR (15 downto 0);
           o_EXT_32 : out  STD_LOGIC_VECTOR (31 downto 0));
end EXT;

architecture arch_EXT of EXT is

begin

	o_EXT_32 <= x"ffff" & i_EXT_16 when Sel = '1' and i_EXT_16(15) ='1' else x"0000" & i_EXT_16;

end arch_EXT;