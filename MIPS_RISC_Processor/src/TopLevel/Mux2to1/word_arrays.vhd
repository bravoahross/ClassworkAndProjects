-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- word_arrays.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 32 32 bit array 
--              
-- 2/3/2023 :: Design created.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
library std;

package word_arrays is 

type reg is array (31 downto 0) of std_logic_vector(31 downto 0);

end word_arrays;