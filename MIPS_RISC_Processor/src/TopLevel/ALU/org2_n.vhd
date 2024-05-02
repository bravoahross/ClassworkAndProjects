-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- org2_n.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a n bit org2
--
--
-- NOTES:
-- 3/8/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity org2_n is
	generic(N : integer := 32);
	Port (  i_A : in std_logic_vector (N-1 downto 0);
		i_B : in std_logic_vector (N-1 downto 0);
		o_F : out std_logic_vector (N-1 downto 0));
end org2_n;

architecture arch_org2_n of org2_n is

	component org2 is 
	  port(i_A          : in std_logic;
      	       i_B          : in std_logic;
      	       o_F          : out std_logic);
	end component;
 
begin

-- Instantiate N org2 instances
G_NBit_ORG: for i in 0 to N-1 generate
    ANDG: org2 port map(
              i_A     => i_A(i),  
              i_B     => i_B(i),  
              o_F      => o_F(i)); 
  end generate G_NBit_ORG;

end arch_org2_n;