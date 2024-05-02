-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- Full_OR.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains or, xor, nor
--
--
-- NOTES:
-- 3/8/23 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity Full_OR is
	generic(N : integer := 32);
	Port (  i_RS  : in  std_logic_vector (N-1 downto 0);
		i_ALUmux : in  std_logic_vector (N-1 downto 0);
		i_S      : in  std_logic_vector (1 downto 0);
		o_F      : out std_logic_vector (N-1 downto 0));
end Full_OR;

architecture arch_Full_OR of Full_OR is

	component org2_n is 
	  port(i_A          : in std_logic_vector (N-1 downto 0);
      	       i_B          : in std_logic_vector (N-1 downto 0);
      	       o_F          : out std_logic_vector (N-1 downto 0));
	end component;

	component xorg2_n is 
	  port(i_A          : in std_logic_vector (N-1 downto 0);
      	       i_B          : in std_logic_vector (N-1 downto 0);
      	       o_F          : out std_logic_vector (N-1 downto 0));
	end component;

	component norg2_n is 
	  port(i_A          : in std_logic_vector (N-1 downto 0);
      	       i_B          : in std_logic_vector (N-1 downto 0);
      	       o_F          : out std_logic_vector (N-1 downto 0));
	end component;

	component mux4_1_n is 
	  port(i_D0          : in std_logic_vector (N-1 downto 0);
      	       i_D1          : in std_logic_vector (N-1 downto 0);
      	       i_D2          : in std_logic_vector (N-1 downto 0);
      	       i_D3          : in std_logic_vector (N-1 downto 0);
      	       i_S           : in std_logic_vector (1 downto 0);
      	       o_O           : out std_logic_vector(N-1 downto 0));
	end component;

-- Signals: 
signal s_D0 : std_logic_vector (N-1 downto 0);
signal s_D1 : std_logic_vector (N-1 downto 0);
signal s_D2 : std_logic_vector (N-1 downto 0);
signal s_D3 : std_logic_vector (N-1 downto 0);
signal s_OUT : std_logic_vector (N-1 downto 0);

begin 

s_D3 <= x"00000000";

G_MUX: mux4_1_n port map(
	i_D0	=>	s_D0,
	i_D1	=>	s_D1,
	i_D2	=>	s_D2,
	i_D3	=>	s_D3,
	i_S	=>	i_S,
	o_O	=>	o_F);


G_OR: org2_n port map(
	i_A	=>	i_RS,
	i_B	=>	i_ALUmux,
	o_F	=>	s_D0);

G_XOR: xorg2_n port map(
	i_A	=>	i_RS,
	i_B	=>	i_ALUmux,
	o_F	=>	s_D1);

G_NOR: norg2_n port map(
	i_A	=>	i_RS,
	i_B	=>	i_ALUmux,
	o_F	=>	s_D2);

end arch_Full_OR;