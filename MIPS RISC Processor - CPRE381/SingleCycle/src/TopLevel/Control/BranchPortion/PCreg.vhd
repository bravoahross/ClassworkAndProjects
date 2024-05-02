-------------------------------------------------------------------------
-- Alex Brown
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- PCreg.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of an N-bit wide 2:1
-- mux using structural VHDL, generics, and generate statements.
--
--
-- NOTES:
-- 2/2/2023 Created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity PCreg is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port(i_WE         : in std_logic;
       i_CLK        : in std_logic;
       i_RST        : in std_logic;
       i_D          : in std_logic_vector(N-1 downto 0);
       o_Q          : out std_logic_vector(N-1 downto 0));

end PCreg;

architecture structure of PCreg is

  component PCdffg is

    port(i_CLK        : in std_logic;     -- Clock input
         i_RST        : in std_logic;     -- Reset input	
         i_RSTval     : in std_logic;     -- Value to reset to
         i_WE         : in std_logic;     -- Write enable input
         i_D          : in std_logic;     -- Data value input
         o_Q          : out std_logic);   -- Data value output
  
  end component;
  signal s_storedReset : std_logic_vector(31 downto 0);

begin

  s_storedReset <= x"00400000";

  -- Instantiate N mux instances.

  G_NBit_REG: for i in 0 to N-1 generate
    REGI: PCdffg port map(
              i_CLK     => i_CLK,   
              i_RST     => i_RST, 
              i_RSTval  => s_storedReset(i),
              i_WE      => i_WE,
              i_D       => i_D(i),  -- ith instance's data 1 input hooked up to ith data 1 input.
              o_Q       => o_Q(i));  -- ith instance's data output hooked up to ith data output.
  end generate G_NBit_REG;


  
end structure;
