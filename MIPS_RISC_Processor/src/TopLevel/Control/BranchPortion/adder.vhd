library IEEE;
use IEEE.std_logic_1164.all;


entity adder is

  port(i_D0 		            : in std_logic;
       i_D1 		            : in std_logic;
       i_C 		            : in std_logic;
       o_O 		            : out std_logic;
       o_C 		            : out std_logic);

end adder;

architecture structure of adder is
  
  -- Describe the component entities as defined in Adder.vhd, Reg.vhd,
  -- Multiplier.vhd, RegLd.vhd (not strictly necessary).

  component andg2
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic);
  end component;

  component invg
    port(i_A          : in std_logic;
         o_F          : out std_logic);
  end component;

  component org2
    port(i_A          : in std_logic;
         i_B          : in std_logic;
         o_F          : out std_logic);


  end component;


  -- Signal to carry stored weight
  signal s_ND0, s_ND1, s_NC : std_logic; -- Carry inverts of input
  signal s_a1,s_a2,s_a3,s_a4,s_a5,s_a6,s_a7,s_a8,s_a9,s_a10,s_a11 : std_logic; -- Outputs of the ands
  signal s_o1,s_o2,s_o3 : std_logic; -- Outputs of the ors

begin

  ---------------------------------------------------------------------------
  -- Level 0: Nots
  ---------------------------------------------------------------------------
 
  g_ND0: invg
    port MAP(i_A             => i_D0,
             o_F             => s_ND0);
  g_ND1: invg
    port MAP(i_A             => i_D1,
             o_F             => s_ND1);
  g_NC: invg
    port MAP(i_A             => i_C,
             o_F             => s_NC);


  ---------------------------------------------------------------------------
  -- Level 1: And Round 1 (1, 3, 5, 7, 9, 10, 11)
  ---------------------------------------------------------------------------
  g_And1: andg2
    port MAP(i_A               => s_ND0,
             i_B               => s_ND1,
             o_F               => s_a1);
  g_And3: andg2
    port MAP(i_A               => s_ND0,
             i_B               => s_NC,
             o_F               => s_a3);
  g_And5: andg2
    port MAP(i_A               => i_D0,
             i_B               => s_ND1,
             o_F               => s_a5);
  g_And7: andg2
    port MAP(i_A               => i_D0,
             i_B               => i_D1,
             o_F               => s_a7);
  g_And9: andg2
    port MAP(i_A               => i_C,
             i_B               => i_D1,
             o_F               => s_a9);
  g_And10: andg2
    port MAP(i_A               => i_D1,
             i_B               => i_D0,
             o_F               => s_a10);
  g_And11: andg2
    port MAP(i_A               => i_D0,
             i_B               => i_C,
             o_F               => s_a11);

    
  ---------------------------------------------------------------------------
  -- Level 2: And Round 2 (2,4,6,8)
  ---------------------------------------------------------------------------
  g_And2: andg2
    port MAP(i_A               => s_a1,
             i_B               => i_C,
             o_F               => s_a2);
  g_And4: andg2
    port MAP(i_A               => s_a3,
             i_B               => i_D1,
             o_F               => s_a4);
  g_And6: andg2
    port MAP(i_A               => s_NC,
             i_B               => s_a5,
             o_F               => s_a6);
  g_And8: andg2
    port MAP(i_A               => i_C,
             i_B               => s_a7,
             o_F               => s_a8);

  ---------------------------------------------------------------------------
  -- Level 3: OR Round 1 (1,2,3)
  ---------------------------------------------------------------------------
  g_Or1: org2
    port MAP(i_A               => s_a2,
             i_B               => s_a4,
             o_F               => s_o1);
 g_Or2: org2
    port MAP(i_A               => s_a6,
             i_B               => s_a8,
             o_F               => s_o2);
 g_Or3: org2
    port MAP(i_A               => s_a10,
             i_B               => s_a11,
             o_F               => s_o3);

  ---------------------------------------------------------------------------
  -- Level 4: OR Round 2 (4,5)
  ---------------------------------------------------------------------------
 g_Or4: org2
    port MAP(i_A               => s_o1,
             i_B               => s_o2,
             o_F               => o_O);
 g_Or5: org2
    port MAP(i_A               => s_o3,
             i_B               => s_a9,
             o_F               => o_C);

  end structure;