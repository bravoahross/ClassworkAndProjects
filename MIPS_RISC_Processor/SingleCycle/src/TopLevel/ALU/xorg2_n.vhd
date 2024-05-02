-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- xorg2_n.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a n bit xorg2
--
--
-- NOTES:
-- 3/8/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity xorg2_n is
	generic(N : integer := 32);
	Port (  i_A : in std_logic_vector (N-1 downto 0);
		i_B : in std_logic_vector (N-1 downto 0);
		o_F : out std_logic_vector (N-1 downto 0));
end xorg2_n;

architecture arch_xorg2_n of xorg2_n is

	component xorg2 is 
	  port(i_A          : in std_logic;
      	       i_B          : in std_logic;
      	       o_F          : out std_logic);
	end component;
 
begin

-- Instantiate N org2 instances
G_NBit_XORG: for i in 0 to N-1 generate
    XORG: xorg2 port map(
              i_A     => i_A(i),  
              i_B     => i_B(i),  
              o_F      => o_F(i)); 
  end generate G_NBit_XORG;

end arch_xorg2_n;