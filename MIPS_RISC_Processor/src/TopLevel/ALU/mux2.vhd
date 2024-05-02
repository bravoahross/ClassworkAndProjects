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


entity mux2 is

  port(i_X 		            : in std_logic;
       i_A                          : in std_logic;
       i_B                          : in std_logic;
       o_F 		            : out std_logic);

end mux2;

architecture structure of mux2 is
 

  component org2
    port(i_A          : in std_logic;
        i_B          : in std_logic;
        o_F          : out std_logic);
   end component;

  component andg2
    port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);
  end component;
  
  component invg
    port(i_A          : in std_logic;
        o_F          : out std_logic);
  end component;

  -- Signal to carry Y and ~S
  signal s_Y         : std_logic;
  -- Signal to carry X and S
  signal s_X   : std_logic;
  -- Signal to carry ~ S
  signal s_S   : std_logic;
	

  begin
  
  g_and1: andg2
      port MAP(i_A             => i_A,
               i_B               => i_X,
               o_F               => s_X);

  g_inv1: invg
      port MAP(i_A            => i_X,
               o_F               => s_S);

  g_and2: andg2
      port MAP(i_A             => i_B,
               i_B               => s_S,
               o_F               => s_Y);

  g_or1: org2
      port MAP(i_A             => s_X,
               i_B               => s_Y,
               o_F               => o_F);

end structure;