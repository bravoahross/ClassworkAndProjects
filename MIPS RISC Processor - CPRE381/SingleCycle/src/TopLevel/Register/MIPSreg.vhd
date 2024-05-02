-------------------------------------------------------------------------
-- Noah Ross
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- MIPSreg.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 32 bit, 32 MIPS register
--              
-- 2/3/2023 :: Design created.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
library std;
library work;
use work.MIPS_types.all;



entity MIPSreg is
  generic(N : integer := 32); -- Generic of type integer for input/output data width. Default value is 32.
  port( i_rs	  : in std_logic_vector(4 downto 0);   -- Read 1 select
	i_rt	  : in std_logic_vector(4 downto 0);   -- Read 2 select
	i_rd	  : in std_logic_vector(4 downto 0);   -- Write select
	i_RST	  : in std_logic; 		       -- Reset
	i_CLK	  : in std_logic; 		       -- Clock
	i_WE	  : in std_logic; 		       -- Write Enable
	i_DATA	  : in std_logic_vector(N-1 downto 0);  -- Data in 
	o_rs	  : out std_logic_vector(N-1 downto 0); -- Read 1 out
	o_rt	  : out std_logic_vector(N-1 downto 0)); -- Read 2 out
end MIPSreg;

architecture MIPSreg_arch of MIPSreg is
  component reg_N is
    port( i_data       :  in std_logic_vector(N-1 downto 0);
          i_reset      :  in std_logic;
          i_clock      :  in std_logic;
          i_WriteEn    :  in std_logic;
          o_data       :  out std_logic_vector(N-1 downto 0));
  end component;

  component MUX32_32to1 is
    port( i_data       :  in reg;
	  o_data       :  out std_logic_vector(N-1 downto 0);
	  i_S          :  in std_logic_vector(4 downto 0));
  end component;

  component dec5_32 is
    port( o_regN       :  out std_logic_vector(N-1 downto 0); 
          i_S          :  in std_logic_vector(4 downto 0);
          i_writeEN    :  in std_logic);
  end component;

signal s_OUT : reg;
signal A : std_logic_vector(N-1 downto 0); 
begin


    Reg0: reg_N port map(            
              i_clock    => i_CLK,
              i_reset    => '1',  
              i_WriteEn  => A(0),
              i_data     => X"00000000",
              o_data     => s_OUT(0));

  -- Instantiate N 32 bit reg instances.
  G_NBitN_REG: for i in 1 to N-1 generate
    Reg: reg_N port map(            
              i_clock    => i_CLK,
              i_reset    => i_RST,  
              i_WriteEn  => A(i),
              i_data     => i_DATA,
              o_data     => s_OUT(i));
  end generate G_NBitN_REG;

  READ_RS: MUX32_32to1
    port MAP( i_data	 => s_OUT,
              o_data     => o_rs, 
              i_S        => i_rs); 

  READ_RT: MUX32_32to1
    port MAP( i_data	 => s_OUT,
              o_data     => o_rt, 
              i_S        => i_rt);

  WRITE_RD: dec5_32
    port MAP( i_S	 => i_rd,
              o_regN     => A,
              i_writeEN  => i_WE);



end MIPSreg_arch;




