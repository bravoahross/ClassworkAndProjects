-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- lab1_3b.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 2-1 MUX
--
--
-- NOTES:
-- 1/26/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity MUX2to1 is

  port( iX			    : in std_logic;
	iY			    : in std_logic;
	S			    : in std_logic;
	O			    : out std_logic);

end MUX2to1;

architecture structure of MUX2to1 is

  component invg
    port(i_A          : in std_logic;
         o_F          : out std_logic);
  end component;

  component andg2
    port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);
  end component;

  component org2
      port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);
  end component;

signal sX         : std_logic;
signal sY         : std_logic;
signal sNOT       : std_logic;

begin

  ---------------------------------------------------------------------------
  -- Level 1: NOT gate
  ---------------------------------------------------------------------------

g_NOT: invg
    port MAP(i_A               => S,
             o_F              => sNOT);

  ---------------------------------------------------------------------------
  -- Level 2: AND gates
  ---------------------------------------------------------------------------

g_AND1: andg2
    port MAP(i_A               => sNOT,
	     i_B               => iX,
             o_F              => sX);

g_AND2: andg2
    port MAP(i_A               => S,
	     i_B               => iY,
             o_F              => sY);

  ---------------------------------------------------------------------------
  -- Level 3: OR gate
  ---------------------------------------------------------------------------

g_OR2: org2
    port MAP(i_A               => sX,
	     i_B               => sY,
             o_F              => O);


end structure;
