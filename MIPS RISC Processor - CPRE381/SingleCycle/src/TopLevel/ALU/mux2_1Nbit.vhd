-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- mux2_1Nbit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains N bit MUX
--
--
-- NOTES:
-- 3/6/23 :: Design created.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity mux2_1Nbit is
  generic(N : integer := 32);
  port(i_A  : in std_logic_vector(N-1 downto 0);
       i_B  : in std_logic_vector(N-1 downto 0);
       i_S  : in std_logic;
       o_F  : out std_logic_vector(N-1 downto 0));

end mux2_1Nbit;



architecture dataflow of mux2_1Nbit is
signal a,b,c : std_logic_vector(N-1 downto 0);
begin
Gmuxes: for i in 0 to N-1 generate
	a(i) <= not i_S;
	b(i) <= a(i) and i_A(i);
	c(i) <= i_B(i) and i_S;
	o_F(i) <= c(i) or b(i);
end generate;
end dataflow;