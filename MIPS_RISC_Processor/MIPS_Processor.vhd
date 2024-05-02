-------------------------------------------------------------------------
-- Henry Duwe
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------
-- MIPS_Processor.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a skeleton of a MIPS_Processor  
-- implementation.

-- 01/29/2019 by H3::Design created.
-------------------------------------------------------------------------
LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;

LIBRARY work;
USE work.MIPS_types.ALL;

ENTITY MIPS_Processor IS
  GENERIC (N : INTEGER := DATA_WIDTH);
  PORT (
    iCLK : IN STD_LOGIC;
    iRST : IN STD_LOGIC;
    iInstLd : IN STD_LOGIC;
    iInstAddr : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
    iInstExt : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
    oALUOut : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0)); -- TODO: Hook this up to the output of the ALU. It is important for synthesis that you have this output that can effectively be impacted by all other components so they are not optimized away.

END MIPS_Processor;
ARCHITECTURE structure OF MIPS_Processor IS

  -- Required data memory signals
  SIGNAL s_DMemWr : STD_LOGIC; -- TODO: use this signal as the final active high data memory write enable signal
  SIGNAL s_DMemAddr : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as the final data memory address input
  SIGNAL s_DMemData : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as the final data memory data input
  SIGNAL s_DMemOut : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as the data memory output

  -- Required register file signals 
  SIGNAL s_RegWr : STD_LOGIC; -- TODO: use this signal as the final active high write enable input to the register file
  SIGNAL s_RegWrAddr : STD_LOGIC_VECTOR(4 DOWNTO 0); -- TODO: use this signal as the final destination register address input
  SIGNAL s_RegWrData : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as the final data memory data input

  -- Required instruction memory signals
  SIGNAL s_IMemAddr : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- Do not assign this signal, assign to s_NextInstAddr instead
  SIGNAL s_NextInstAddr : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as your intended final instruction memory address input.
  SIGNAL s_Inst : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- TODO: use this signal as the instruction signal 

  -- Required halt signal -- for simulation
  SIGNAL s_Halt : STD_LOGIC; -- TODO: this signal indicates to the simulation that intended program execution has completed. (Opcode: 01 0100)

  -- Required overflow signal -- for overflow exception detection
  SIGNAL s_Ovfl : STD_LOGIC; -- TODO: this signal indicates an overflow exception would have been initiated
  -- Signals made by Noah
  SIGNAL s_ALUSrc : STD_LOGIC; -- This connects control to AlU mux
  SIGNAL s_MemToReg : STD_LOGIC; -- This connects control to Mem to Reg mux
  SIGNAL s_oRT : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); --This connects REG FILE o_rt to ALU MUX i_D0
  SIGNAL s_iEXT : STD_LOGIC_VECTOR(N - 1 DOWNTO 0); --This is the sign extended immediate 
  SIGNAL s_bALU : STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
  SIGNAL s_ALUctrl : STD_LOGIC_VECTOR(5 DOWNTO 0);
  SIGNAL s_shamt : std_logic_vector(4 DOWNTO 0);
  SIGNAL s_ALUresult : STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
  SIGNAL s_Zero_bit : STD_LOGIC;
  SIGNAL s_CARRY_unused : STD_LOGIC;
  SIGNAL s_EXTsign : STD_LOGIC;

  -- Signals by Alex
  SIGNAL s_MemWriteData : STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
  SIGNAL s_JALWriteData : STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
  SIGNAL s_oRS : STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
  SIGNAL s_wrReg : STD_LOGIC_VECTOR(4 DOWNTO 0);
  SIGNAL s_WrEn : STD_LOGIC;
  SIGNAL s_WrEnAnd : STD_LOGIC;
  SIGNAL s_WrEnIn : STD_LOGIC;
  SIGNAL s_RegWrSel : STD_LOGIC;
  SIGNAL s_RegDst : STD_LOGIC_Vector(1 downto 0);
  signal s_RegWrDataSel : Std_Logic_vector(1 downto 0);
  SIGNAL s_Jump : STD_LOGIC;
  SIGNAL s_Branch : STD_LOGIC;
  SIGNAL s_JR : STD_LOGIC;
  SIGNAL s_JAL : STD_LOGIC;
  SIGNAL s_MemRead : STD_LOGIC;
  SIGNAL s_MemWrite : STD_LOGIC;
  SIGNAL s_branchSel : STD_LOGIC_vector(2 downto 0);
  SIGNAL s_ALU_slt : STD_LOGIC;
  SIGNAL s_LUI : STD_LOGIC_VECTOR(31 DOWNTO 0);
  SIGNAL s_wrDataMUX : STD_LOGIC_VECTOR(31 DOWNTO 0);

  COMPONENT mem IS
    GENERIC (
      ADDR_WIDTH : INTEGER;
      DATA_WIDTH : INTEGER);
    PORT (
      clk : IN STD_LOGIC;
      addr : IN STD_LOGIC_VECTOR((ADDR_WIDTH - 1) DOWNTO 0);
      data : IN STD_LOGIC_VECTOR((DATA_WIDTH - 1) DOWNTO 0);
      we : IN STD_LOGIC := '1';
      q : OUT STD_LOGIC_VECTOR((DATA_WIDTH - 1) DOWNTO 0));
  END COMPONENT;

  COMPONENT MIPSreg IS
    GENERIC (N : INTEGER := 32); -- Generic of type integer for input/output data width. Default value is 32.
    PORT (
      i_rs : IN STD_LOGIC_VECTOR(4 DOWNTO 0); -- Read 1 select
      i_rt : IN STD_LOGIC_VECTOR(4 DOWNTO 0); -- Read 2 select
      i_rd : IN STD_LOGIC_VECTOR(4 DOWNTO 0); -- Write select
      i_RST : IN STD_LOGIC; -- Reset
      i_CLK : IN STD_LOGIC; -- Clock
      i_WE : IN STD_LOGIC; -- Write Enable
      i_DATA : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- Data in 
      o_rs : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0); -- Read 1 out
      o_rt : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0)); -- Read 2 out
  END COMPONENT;

  COMPONENT ALU IS
    GENERIC (N : INTEGER := 32);
    PORT (
      i_aALU : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
      i_bALU : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
      i_Scontrol : IN STD_LOGIC_VECTOR(5 DOWNTO 0);
      i_shamt : IN STD_LOGIC_VECTOR(4 DOWNTO 0);
      o_OUT : OUT STD_LOGIC_VECTOR(31 DOWNTO 0);
      o_C : OUT STD_LOGIC;
      o_Over : OUT STD_LOGIC;
      o_Zero : OUT STD_LOGIC);
  END COMPONENT;

  COMPONENT mux2t1_N IS
    GENERIC (N : INTEGER := 16); -- Generic of type integer for input/output data width. Default value is 32.
    PORT (
      i_S : IN STD_LOGIC;
      i_D0 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      i_D1 : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      o_O : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0));
  END COMPONENT;

  COMPONENT EXT is
    Port ( Sel : in  STD_LOGIC;
	         i_EXT_16 : in  STD_LOGIC_VECTOR (15 downto 0);
           o_EXT_32 : out  STD_LOGIC_VECTOR (31 downto 0));
  end component;

  COMPONENT fetch IS
    GENERIC (N : INTEGER := 32);
    PORT (
      i_instr : IN STD_LOGIC_VECTOR(25 DOWNTO 0);
      i_ctrl_jump : IN STD_LOGIC;
      i_ctrl_branch : IN STD_LOGIC;
      i_ctrl_jr : IN STD_LOGIC;
      i_ALU_zero : IN STD_LOGIC;
      i_branch_sel : IN STD_LOGIC_VECTOR(2 DOWNTO 0);
      i_ALU_slt : IN STD_LOGIC;
      i_extended : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      i_jr : IN STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      i_Clk : IN STD_LOGIC;
      i_RST : IN STD_LOGIC;
      o_read_addr : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      o_pcP4 : OUT STD_LOGIC_VECTOR(N - 1 DOWNTO 0);
      o_brWR : OUT STD_LOGIC);
  END COMPONENT;

  COMPONENT andg2
    PORT (
      i_A : IN STD_LOGIC;
      i_B : IN STD_LOGIC;
      o_F : OUT STD_LOGIC);
  END COMPONENT;

  COMPONENT LUI
    PORT (
      I_LUI : IN STD_LOGIC_VECTOR (15 DOWNTO 0);
      O_LUI : OUT STD_LOGIC_VECTOR (31 DOWNTO 0));
  END COMPONENT;

  Component MUX2to1 is

    port( iX			    : in std_logic;
    iY			    : in std_logic;
    S			    : in std_logic;
    O			    : out std_logic);
    end Component;

  COMPONENT control IS
    PORT (
      i_Instruction : IN STD_LOGIC_VECTOR (31 DOWNTO 0);
      o_RegDst : OUT STD_LOGIC_Vector(1 downto 0);
      o_RegWrDataSel : out STD_Logic_vector(1 downto 0);
      o_Jump : OUT STD_LOGIC;
      o_Branch : OUT STD_LOGIC;
      o_BranchSel : out std_logic_vector(2 downto 0);
      o_JR : OUT STD_LOGIC;
      o_JAL : OUT STD_LOGIC;
      o_MemRead : OUT STD_LOGIC;
      o_MemtoReg : OUT STD_LOGIC;
      o_MemWrite : OUT STD_LOGIC;
      o_ALUSrc : OUT STD_LOGIC;
      o_RegWrite : OUT STD_LOGIC;
      o_RegWriteSel : OUT STD_LOGIC;
      o_Halt : OUT STD_LOGIC;
      o_EXT : OUT STD_LOGIC;
      o_ALUOp : OUT STD_LOGIC_VECTOR (5 DOWNTO 0));
  END COMPONENT;

  -- TODO: You may add any additional signals or components your implementation 
  --       requires below this comment

BEGIN

  -- TODO: This is required to be your final input to your instruction memory. This provides a feasible method to externally load the memory module which means that the synthesis tool must assume it knows nothing about the values stored in the instruction memory. If this is not included, much, if not all of the design is optimized out because the synthesis tool will believe the memory to be all zeros.

  WITH iInstLd SELECT
    s_IMemAddr <= s_NextInstAddr WHEN '0',
    iInstAddr WHEN OTHERS;
  IMem : mem
  GENERIC MAP(
    ADDR_WIDTH => ADDR_WIDTH,
    DATA_WIDTH => N)
  PORT MAP(
    clk => iCLK,
    addr => s_IMemAddr(11 DOWNTO 2),
    data => iInstExt,
    we => iInstLd,
    q => s_Inst);

  DMem : mem
  GENERIC MAP(
    ADDR_WIDTH => ADDR_WIDTH,
    DATA_WIDTH => N)
  PORT MAP(
    clk => iCLK,
    addr => s_DMemAddr(11 DOWNTO 2),
    data => s_DMemData,
    we => s_DMemWr,
    q => s_DMemOut);

  ControlUnit : control
  PORT MAP(
    i_Instruction => s_Inst,
    o_RegDst => s_RegDst,
    o_RegWrDataSel => s_RegWrDataSel,
    o_Jump => s_Jump,
    o_Branch => s_Branch,
    o_BranchSel => s_BranchSel,
    o_JR => s_JR,
    o_JAL => s_JAL,
    o_MemRead => s_MemRead,
    o_MemtoReg => s_MemToReg,
    o_MemWrite => s_DMemWr,
    o_ALUSrc => s_ALUSrc,
    o_RegWrite => s_RegWr,
    o_Halt => s_Halt,
    o_EXT => s_EXTsign,
    o_RegWriteSel => s_RegWrSel,
    o_ALUOp => s_ALUctrl);

  WrAddrMUX1 : mux2t1_N -- Selecting the write destinations, rd or rt or 31
  GENERIC MAP(N => 5)
  PORT MAP(
    i_D0 => s_Inst(20 DOWNTO 16), -- rt
    i_D1 => s_wrReg,
    i_S => s_RegDst(0), -- X0 is rt
    o_O => s_RegWrAddr);

  WrAddrMUX2 : mux2t1_N -- 11 is 31, 01 is rd,
  GENERIC MAP(N => 5)
  PORT MAP(
    i_D0 => s_Inst(15 DOWNTO 11), -- rd
    i_D1 => "11111",
    i_S => s_RegDst(1),
    o_O => s_wrReg);

  regWRAnd : andg2
  PORT MAP(
    i_A => s_RegWr,
    i_B => s_WrEnIn,
    o_F => s_WrEnAnd);

  WrEnMUX : MUX2to1
  PORT MAP(
    iX => s_RegWr,
    iY => s_WrEnAnd,
    S => s_RegWrSel,
    O => s_WrEn);

  RegFile : MIPSreg
  GENERIC MAP(N => 32)
  PORT MAP(
    i_rs => s_Inst(25 DOWNTO 21),
    i_rt => s_Inst(20 DOWNTO 16),
    i_rd => s_RegWrAddr,
    i_RST => iRST,
    i_CLK => iCLK,
    i_WE => s_WrEn,
    i_DATA => s_RegWrData,
    o_rs => s_oRS,
    o_rt => s_oRT);

  s_shamt <= s_Inst(10 DOWNTO 6);
  s_DMemData <= s_oRT;        --   attempt to fix store word, not sure what is wrong

  ALUUnit : ALU
  PORT MAP(
    i_aALU => s_oRS,
    i_bALU => s_bALU,
    i_Scontrol => s_ALUctrl,
    i_shamt => s_shamt,
    o_OUT => s_ALUresult,
    o_C => s_CARRY_unused, 
    o_Over => s_Ovfl, -- DOES NOT WORK RIGHT
    o_Zero => s_Zero_bit);

  s_DMemAddr <= s_ALUresult;
  s_ALU_slt <= s_ALUresult(0);
  oALUOut <= s_ALUresult;

  Extender : EXT
  PORT MAP(
    Sel => s_EXTsign,
    i_EXT_16 => s_Inst(15 DOWNTO 0),
    o_EXT_32 => s_iEXT);

  FetchUnit : fetch
  PORT MAP(
    i_instr => s_Inst(25 downto 0),
    i_ctrl_jump => s_Jump,
    i_ctrl_branch => s_Branch,
    i_ctrl_jr => s_JR,
    i_ALU_zero => s_Zero_bit,
    i_extended => s_iEXT,
    i_ALU_slt => s_ALU_slt,
    i_branch_sel => s_branchsel,
    i_jr => s_oRS,
    i_Clk => iCLK,
    i_RST => iRST,
    o_read_addr => s_NextInstAddr,
    o_pcP4 => s_JALWriteData,
    o_brWr => s_WrEnIn);

  luiUnit : lui
  PORT MAP(
    i_LUI => s_Inst(15 DOWNTO 0),
    o_LUI => s_LUI);

  WriteDataMux : mux2t1_N -- 11 is jal, 00 is normal, 01 is lui
  GENERIC MAP(N => 32)
  PORT MAP(
    i_S => s_RegWrDataSel(0),
    i_D0 => s_MemWriteData,
    i_D1 => s_wrDataMux,
    o_O => s_RegWrData);

  WriteDataMux2 : mux2t1_N
  GENERIC MAP(N => 32)
  PORT MAP(
    i_S => s_RegWrDataSel(1),
    i_D0 => s_LUI,
    i_D1 => s_JALWriteData,
    o_O => s_wrDataMux);

  ALUMux : mux2t1_N
  GENERIC MAP(N => 32)
  PORT MAP(
    i_S => s_ALUSrc,
    i_D0 => s_oRT,
    i_D1 => s_iEXT,
    o_O => s_bALU);

  MemToRegMux : mux2t1_N  -- decides dmem return  or a register return to the reg
  GENERIC MAP(N => 32)
  PORT MAP(
    i_S => s_MemToReg,
    i_D0 => s_ALUresult,
    i_D1 => s_DMemOut,
    o_O => s_MemWriteData);

  -- TODO: Ensure that s_Halt is connected to an output control signal produced from decoding the Halt instruction (Opcode: 01 0100)

  -- TODO: Implement the rest of your processor below this comment! 

END structure;
