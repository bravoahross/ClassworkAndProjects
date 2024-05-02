-------------------------------------------------------------------------
-- Alex Brown
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- mux2t1.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of a 2:1 Multiplexer.
--
--
-- NOTES:
-- 1/26/2023 Initial Creation
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux2t1 is

  port(i_S          : in std_logic;
       i_D0         : in std_logic;
       i_D1         : in std_logic;
       o_O          : out std_logic);

end mux2t1;

architecture structure of mux2t1 is
  component andg2
    port(i_A	: in std_logic;
	 i_B	: in std_logic;
	 o_F	: out std_logic);

  end component;
  
  component org2
    port(i_A          : in std_logic;
         i_B          : in std_logic;
         o_F          : out std_logic);
  end component;

  component invg
    port(i_A          : in std_logic;
         o_F          : out std_logic);
  end component;


  -- Signal to carry out from ands
  signal s_A1, s_A2   : std_logic;
  -- Signal to carry inverse i_S
  signal s_Sn : std_logic;
  
begin

	---------------------------------------------------------------------------
  	-- Level 0: Make a NOT of Select
  	---------------------------------------------------------------------------
	g_Not: invg
	   port MAP(i_A  => i_S,
		    o_F  => s_Sn);
   

	---------------------------------------------------------------------------
  	-- Level 1: The Ands
  	---------------------------------------------------------------------------

	g_And0: andg2
	  port MAP(i_A  => i_D0,
	           i_B  => s_Sn,
		   o_F  => s_A1);

	g_And1: andg2
	  port MAP(i_A  => i_D1,
	           i_B  => i_S,
		   o_F  => s_A2);

	---------------------------------------------------------------------------
  	-- Level 3: Or and Out
  	---------------------------------------------------------------------------
	g_Or: org2
	  port MAP(i_A  => s_A1,
		   i_B  => s_A2,
		   o_F  => o_O);

end structure;

