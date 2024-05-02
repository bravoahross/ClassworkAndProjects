---------------------------------------------------------
-- Noah Ross & Alex Brown
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- mux8to1.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 4 bit mux
--
--
-- NOTES:
-- 3/6/23 :: Design created.
-------------------------------------------------------------------------

LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;

ENTITY mux8to1 IS
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
END mux8to1;

ARCHITECTURE arch_mux8 OF mux8to1 IS

BEGIN

o_F <= i_A when i_S = "000" else 
       i_B when i_S = "001" else 
       i_C when i_S = "010" else 
       i_D when i_S = "011" else 
       i_E when i_S = "100" else 
       i_F when i_S = "101" else 
       i_G when i_S = "110" else 
       i_H when i_S = "111" else i_H;


    -- PROCESS (i_A, i_B, i_C, i_D, i_S, o_F) IS
    -- BEGIN
    --     IF i_S(2 DOWNTO 0) = "000" THEN
    --         o_F <= i_A;

    --     ELSIF i_S(2 DOWNTO 0) = "001" THEN
    --         o_F <= i_B;

    --     ELSIF i_S(2 DOWNTO 0) = "010" THEN
    --         o_F <= i_C;

    --     ELSIF i_S(2 DOWNTO 0) = "011" THEN
    --         o_F <= i_D;

    --     ELSIF i_S(2 DOWNTO 0) = "100" THEN
    --         o_F <= i_E;

    --     ELSIF i_S(2 DOWNTO 0) = "101" THEN
    --         o_F <= i_F;

    --     ELSIF i_S(2 DOWNTO 0) = "110" THEN
    --         o_F <= i_G;

    --     ELSIF i_S(2 DOWNTO 0) = "111" THEN
    --         o_F <= i_H;
    --     END IF;
    -- END PROCESS;
END arch_mux8;