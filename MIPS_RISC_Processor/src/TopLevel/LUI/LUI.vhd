-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- LUI.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains LUI extender
--
--
-- NOTES:
-- 3/26/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity LUI is
    Port (
	   I_LUI : in  STD_LOGIC_VECTOR (15 downto 0);
           O_LUI : out  STD_LOGIC_VECTOR (31 downto 0));
end LUI;

architecture arch_LUI of LUI is

begin

O_LUI <= I_LUI & x"0000"; 

  -- process(I_LUI)
  --   begin
	-- if I_LUI = I_LUI then
	-- 	O_LUI <= I_LUI & x"0000";
  -- end if;
  -- end process;
  
end arch_LUI;