-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- mux4_1_n.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a N bit 4 to 1 mux
--
--
-- NOTES:
-- 3/6/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux4_1_n is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port(i_D0                         : in std_logic_vector(N-1 downto 0);
       i_D1 		            : in std_logic_vector(N-1 downto 0);
       i_D2 		            : in std_logic_vector(N-1 downto 0);
       i_D3 		            : in std_logic_vector(N-1 downto 0);
       i_S			    : in std_logic_vector(1 downto 0);
       o_O 		            : out std_logic_vector(N-1 downto 0));

end mux4_1_n;

architecture structural of mux4_1_n is

  component mux4_1 is
    port(i_A                        : in std_logic;
         i_B 		            : in std_logic;
         i_C 		            : in std_logic;
         i_D 		            : in std_logic;
         i_S			    : in std_logic_vector(1 downto 0);
         o_F 		            : out std_logic);
  end component;

begin

  -- Instantiate N mux instances.
  G_NBit_MUX: for i in 0 to N-1 generate
    MUXI: mux4_1 port map(
              i_S      => i_S,     -- All instances share the same select input.
              i_A     => i_D0(i),  -- ith instance's data 0 input hooked up to ith data 0 input.
              i_B     => i_D1(i),  -- ith instance's data 1 input hooked up to ith data 1 input.
              i_C     => i_D2(i),  -- ith instance's data 1 input hooked up to ith data 1 input.
              i_D     => i_D3(i),  -- ith instance's data 1 input hooked up to ith data 1 input.
              o_F      => o_O(i)); -- ith instance's data output hooked up to ith data output.
  end generate G_NBit_MUX;
  
end structural;