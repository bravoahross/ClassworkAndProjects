-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- dec5_32.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 5 to 32 bit decoder
--              
-- 1/30/2023 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
library std;

entity dec5_32 is

  port(o_regN     : out std_logic_vector(31 downto 0);  -- register number selected
       i_S        : in std_logic_vector(4 downto 0);   -- select bits   
	   i_writeEN  : in std_logic);
end dec5_32;

architecture mixed of dec5_32 is

signal s_Sel : std_logic_vector(5 downto 0);

   begin 

	s_Sel <= i_S & i_writeEN;

     with (s_Sel) select
	o_regN  <= "00000000000000000000000000000001" when "000001",
 		   "00000000000000000000000000000010" when "000011",
 		   "00000000000000000000000000000100" when "000101",
 		   "00000000000000000000000000001000" when "000111",
 		   "00000000000000000000000000010000" when "001001",
 		   "00000000000000000000000000100000" when "001011",
 		   "00000000000000000000000001000000" when "001101",
 		   "00000000000000000000000010000000" when "001111",
 		   "00000000000000000000000100000000" when "010001",
 		   "00000000000000000000001000000000" when "010011",
 		   "00000000000000000000010000000000" when "010101",
 		   "00000000000000000000100000000000" when "010111",
 		   "00000000000000000001000000000000" when "011001",
 		   "00000000000000000010000000000000" when "011011",
 		   "00000000000000000100000000000000" when "011101",
 		   "00000000000000001000000000000000" when "011111",
 		   "00000000000000010000000000000000" when "100001",
 		   "00000000000000100000000000000000" when "100011",
 		   "00000000000001000000000000000000" when "100101",
 		   "00000000000010000000000000000000" when "100111",
 		   "00000000000100000000000000000000" when "101001",
 		   "00000000001000000000000000000000" when "101011",
 		   "00000000010000000000000000000000" when "101101",
 		   "00000000100000000000000000000000" when "101111",
 		   "00000001000000000000000000000000" when "110001",
 		   "00000010000000000000000000000000" when "110011",
 		   "00000100000000000000000000000000" when "110101",
 		   "00001000000000000000000000000000" when "110111",
 		   "00010000000000000000000000000000" when "111001",
 		   "00100000000000000000000000000000" when "111011",
 		   "01000000000000000000000000000000" when "111101",
 		   "10000000000000000000000000000000" when "111111",
 		   "00000000000000000000000000000000" when others;
end mixed;