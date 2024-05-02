-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- FullAdd.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a Full Adder
--
--
-- NOTES:
-- 1/27/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity FullAdd is 

port( iX		    : in std_logic;
      iY		    : in std_logic;
      iZ		    : in std_logic;
      S 		    : out std_logic;
      C		            : out std_logic);

end FullAdd;

architecture structure of FullAdd is

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

  component xorg2
      port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);
  end component;

signal A         : std_logic;
signal B         : std_logic;
signal D         : std_logic;

begin


  ---------------------------------------------------------------------------
  -- Level 1: XOR1 and AND1 gates
  ---------------------------------------------------------------------------

g_XOR1: xorg2
    port MAP(i_A               => iX,
             i_B               => iY,
             o_F               => A);

g_AND1: andg2
    port MAP(i_A               => iX,
             i_B               => iY,
             o_F               => B);

  ---------------------------------------------------------------------------
  -- Level 2: AND2 gate
  ---------------------------------------------------------------------------

g_AND2: andg2
    port MAP(i_A               => A,
             i_B               => iZ,
             o_F               => D);

  ---------------------------------------------------------------------------
  -- Level 3: XOR2 and OR gates
  ---------------------------------------------------------------------------

g_XOR2: xorg2
    port MAP(i_A               => A,
             i_B               => iZ,
             o_F               => S);

g_OR: org2
    port MAP(i_A               => B,
             i_B               => D,
             o_F               => C);


end structure;