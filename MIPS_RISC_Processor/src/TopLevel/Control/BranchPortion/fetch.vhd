-------------------------------------------------------------------------
-- Alex Brown
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- fetch.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains fetch functionality.
--
--
-- NOTES:
-- 3/9/23 :: Design created.
-------------------------------------------------------------------------

LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;

ENTITY fetch IS
    GENERIC (N : INTEGER := 32);
    PORT (
        i_instr : IN STD_LOGIC_VECTOR(25 DOWNTO 0);
        i_ctrl_jump : IN STD_LOGIC;
        i_ctrl_branch : IN STD_LOGIC;
        i_ctrl_jr : IN STD_LOGIC;
        i_ALU_zero : IN STD_LOGIC;
        i_extended : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
        i_ALU_slt : IN STD_LOGIC;
        i_branch_sel : IN STD_LOGIC_VECTOR(2 DOWNTO 0);
        i_jr : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
        i_Clk : IN STD_LOGIC;
        i_RST : IN STD_LOGIC;
        o_read_addr : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
        o_pcP4 : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
        o_brWR : out std_logic);

END fetch;

ARCHITECTURE structure OF fetch IS
    COMPONENT adder_N
        GENERIC (N : INTEGER := 32); -- Generic of type integer for input/output data width. Default value is 32.
        PORT (
            i_C : IN STD_LOGIC;
            i_D0 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            i_D1 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            o_O : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            o_C : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT mux2t1_N
        GENERIC (N : INTEGER := 16); -- Generic of type integer for input/output data width. Default value is 32.
        PORT (
            i_S : IN STD_LOGIC;
            i_D0 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            i_D1 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            o_O : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0));
    END COMPONENT;

    COMPONENT mux8to1
        PORT (
            i_A : IN STD_LOGIC;
            i_B : IN STD_LOGIC;
            i_C : IN STD_LOGIC;
            i_D : IN STD_LOGIC;
            i_E : IN STD_LOGIC;
            i_F : IN STD_LOGIC;
            i_G : IN STD_LOGIC;
            i_H : IN STD_LOGIC;
            i_S : IN STD_LOGIC_VECTOR (2 DOWNTO 0);
            o_F : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT andg2
        PORT (
            i_A : IN STD_LOGIC;
            i_B : IN STD_LOGIC;
            o_F : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT invg
        PORT (
            i_A : IN STD_LOGIC;
            o_F : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT org2
        PORT (
            i_A : IN STD_LOGIC;
            i_B : IN STD_LOGIC;
            o_F : OUT STD_LOGIC);
    END COMPONENT;
    COMPONENT PCreg
        GENERIC (N : INTEGER := 32); -- Generic of type integer for input/output data width. Default value is 32.
        PORT (
            i_WE : IN STD_LOGIC;
            i_CLK : IN STD_LOGIC;
            i_RST : IN STD_LOGIC;
            i_D : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
            o_Q : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0));
    END COMPONENT;

    SIGNAL s_pcOut, s_add4Out, s_jumpAddr, s_AddIn, s_add2Out, s_MUX0, s_MUX1, s_MUX2 : STD_LOGIC_VECTOR(31 DOWNTO 0);
    SIGNAL s_and, s_zeroNot, s_greaterEqual, s_sltNot, s_lessEqual, s_branchSel : STD_LOGIC;

    --signal

BEGIN

    g_pcreg : PCreg
    PORT MAP(
        i_WE => '1',
        i_RST => i_RST,
        i_CLK => i_Clk,
        i_D => s_MUX2,
        o_Q => s_pcOut);

    g_add4 : adder_N
    PORT MAP(
        i_C => '0',
        i_D0 => s_pcOut,
        i_D1 => "00000000000000000000000000000100",
        o_O => s_add4Out);

    s_jumpAddr(31 DOWNTO 28) <= s_add4Out(31 DOWNTO 28);
    s_jumpAddr(27 DOWNTO 2) <= i_instr;
    s_jumpAddr(1) <= '0';
    s_jumpAddr(0) <= '0';

    s_addIn(31 DOWNTO 2) <= i_extended(29 DOWNTO 0);
    s_addIn(1) <= '0';
    s_addIn(0) <= '0';

    g_add2 : adder_N
    PORT MAP(
        i_C => '0',
        i_D0 => s_add4Out,
        i_D1 => s_addIn,
        o_O => s_add2Out);

    g_or0 : org2
    PORT MAP(
        i_A => i_ALU_slt,
        i_B => i_ALU_zero,
        o_F => s_lessEqual);

    g_or1 : org2
    Port map(
        i_A => i_ALU_zero,
        i_B => s_sltNot,
        o_F => s_greaterEqual);

    sltNot : invg
    port map(
        i_A => i_ALU_slt,
        o_F => s_sltNot);

    zeroNot : invg
    port map(
        i_A => i_ALU_zero,
        o_F => s_zeroNot);

    branchSel : mux8to1
    port map(
        i_A => i_ALU_zero, -- equal
        i_B => s_zeroNot, -- not equal
        i_C => s_greaterEqual, -- greaterthan or equal
        i_D => s_sltNot, -- greaterthan
        i_E => s_lessEqual, -- lessthan or equal
        i_F => i_ALU_slt, -- lessthan
        i_G => '0',
        i_H => '0',
        i_S => i_branch_sel,
        o_F => s_branchSel);

    g_and : andg2
    PORT MAP(
        i_A => i_ctrl_branch,
        i_B => s_branchSel,
        o_F => s_and);

    g_MUX0 : mux2t1_N -- for jr
    GENERIC MAP(N => 32)
    PORT MAP(
        i_s => i_ctrl_jr,
        i_D0 => s_add4Out,
        i_D1 => i_jr,
        o_O => s_MUX0);

    g_MUX1 : mux2t1_N -- for branching
    GENERIC MAP(N => 32)
    PORT MAP(
        i_S => s_and,
        i_D0 => s_MUX0,
        i_D1 => s_add2Out,
        o_O => s_MUX1);

    g_MUX2 : mux2t1_N -- for jumps
    GENERIC MAP(N => 32)
    PORT MAP(
        i_S => i_ctrl_jump,
        i_D0 => s_MUX1,
        i_D1 => s_jumpAddr,
        o_O => s_MUX2);

    o_read_addr <= s_pcOut;
    o_pcP4 <= s_add4Out;
    o_brWr <= s_and;

END structure;