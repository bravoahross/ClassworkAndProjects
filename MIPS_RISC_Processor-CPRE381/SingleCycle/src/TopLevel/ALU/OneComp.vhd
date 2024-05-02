-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- OneComp.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 2-1 MUX
--
--
-- NOTES:
-- 1/27/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity OneComp is 

port( iX		    : in std_logic;
      O		            : out std_logic);

end OneComp;

architecture structure of OneComp is

  component invg
    port(i_A          : in std_logic;
         o_F          : out std_logic);
  end component;
begin

g_NOT: invg
    port MAP(i_A               => iX,
             o_F              => O);

end structure;
