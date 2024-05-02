-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- Add_Sub_N.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a N bit Full Adder and subtractor
--
--
-- NOTES:
-- 1/30/23 :: Created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity Add_Sub_N is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port(i_X          : in std_logic_vector(N-1 downto 0);
       i_Y          : in std_logic_vector(N-1 downto 0);
       nAdd_Sub     : in std_logic;
       o_C          : out std_logic;
       o_Overflow   : out std_logic;
       o_S          : out std_logic_vector(N-1 downto 0));

end Add_Sub_N;

architecture structural of Add_Sub_N is
  component FullAdd is
    port(iX		    : in std_logic;
         iY		    : in std_logic;
         iZ		    : in std_logic;
         S 		    : out std_logic;
         C		    : out std_logic);
  end component;

  component OneComp is
    port( iX		    : in std_logic;
          O		    : out std_logic);
  end component;

  component MUX2to1 is
    port(iX	            : in std_logic;
	 iY	            : in std_logic;
	 S		    : in std_logic;
	 O		    : out std_logic);
  end component;
signal T       :std_logic_vector(N downto 0);
signal R       :std_logic_vector(N-1 downto 0);
signal Yin     :std_logic_vector(N-1 downto 0);
signal s_C1    :std_logic;
signal s_o_C   :std_logic;

begin
T(0) <= nAdd_Sub;
s_o_C <= T(N);
s_C1 <= T(N-1);
o_C <= s_o_C;

o_Overflow <= s_o_C xor s_C1;

  -- Instantiate N Add/sub instances.
  G_NBit_ADDSUB: for i in 0 to N-1 generate
    INV: OneComp port map(   
              iX     => i_Y(i),
              O      => R(i));
    MUX: MUX2to1 port map(
              iX     => i_Y(i), 
              iY     => R(i), 
              S      => nAdd_Sub, 
              O      => Yin(i)); 
    ADD: FullAdd port map(            
              S      => o_S(i),
              iX     => i_X(i),  
              iY     => Yin(i),
              iZ     => T(i),
              C      => T(i+1));

  end generate G_NBit_ADDSUB;

end structural;