-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- reg_N.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a N bit register
--
--
-- NOTES:
-- 1/30/23 :: Created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity reg_N is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port(i_data       : in std_logic_vector(N-1 downto 0);
       i_reset      : in std_logic;
       i_clock      : in std_logic;
       i_WriteEn    : in std_logic;
       o_data       : out std_logic_vector(N-1 downto 0));
end reg_N;

architecture structural of reg_N is
  component dffg is
    port(i_CLK        : in std_logic;     -- Clock input
         i_RST        : in std_logic;     -- Reset input
         i_WE         : in std_logic;     -- Write enable input
         i_D          : in std_logic;     -- Data value input
         o_Q          : out std_logic);   -- Data value output
  end component;

begin

  -- Instantiate N reg instances.
  G_NBit_REG: for i in 0 to N-1 generate
    DFF: dffg port map(            
              i_CLK    => i_clock,
              i_RST    => i_reset,  
              i_WE     => i_WriteEn,
              i_D      => i_data(i),
              o_Q      => o_data(i));

  end generate G_NBit_REG;

end structural;