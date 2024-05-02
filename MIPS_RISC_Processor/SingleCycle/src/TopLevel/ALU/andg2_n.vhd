-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- andg2_n.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a n bit andg2
--
--
-- NOTES:
-- 3/8/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity andg2_n is
	generic(N : integer := 32);
	Port (  i_A : in std_logic_vector (N-1 downto 0);
		i_B : in std_logic_vector (N-1 downto 0);
		o_F : out std_logic_vector (N-1 downto 0));
end andg2_n;

architecture arch_andg2_n of andg2_n is

	component andg2 is 
	  port(i_A          : in std_logic;
      	       i_B          : in std_logic;
      	       o_F          : out std_logic);
	end component;
 
begin

-- Instantiate N andg2 instances
G_NBit_ANDG: for i in 0 to N-1 generate
    ANDG: andg2 port map(
              i_A     => i_A(i),  
              i_B     => i_B(i),  
              o_F      => o_F(i)); 
  end generate G_NBit_ANDG;

end arch_andg2_n;