-------------------------------------------------------------------------
-- Alex Brown
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- adder_N.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of an N-bit wide 2:1
-- mux using structural VHDL, generics, and generate statements.
--
--
-- NOTES:
-- 1/6/20 by H3::Created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity adder_N is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port(i_C          : in std_logic;
       i_D0         : in std_logic_vector(N-1 downto 0);
       i_D1         : in std_logic_vector(N-1 downto 0);
       o_O          : out std_logic_vector(N-1 downto 0);
       o_C          : out std_logic);

end adder_N;

architecture structural of adder_N is

  component adder is
    port(i_D0 		            : in std_logic;
         i_D1 		            : in std_logic;
         i_C 		            : in std_logic;
         o_O 		            : out std_logic;
         o_C 		            : out std_logic);
  end component;
signal s_C : std_logic_vector(N-1 downto 0);

begin

  -- Instantiate N mux instances.
  ADD1: adder port map(
              i_C      => i_C,
	      i_D0     => i_D0(0),
              i_D1     => i_D1(0),
              o_O      => o_O(0),
              o_C      => s_C(0));
  G_NBit_ADD: for i in 1 to N-2 generate
    ADDI: adder port map(
              i_C      => s_C(i-1),      -- All instances share the same select input.
              i_D0     => i_D0(i),  -- ith instance's data 0 input hooked up to ith data 0 input.
              i_D1     => i_D1(i),  -- ith instance's data 1 input hooked up to ith data 1 input.
              o_O      => o_O(i),  -- ith instance's data output hooked up to ith data output.
              o_C      => s_C(i));

  end generate G_NBit_ADD;
  ADDE: adder port map(
              i_C      => s_C(N-2),
	      i_D0     => i_D0(N-1),
              i_D1     => i_D1(N-1),
              o_O      => o_O(N-1),
              o_C      => o_C);

end structural;