package fft

import chisel3._
import chisel3.util._

// TODO : CHANGE TO SUPPORT COMPLEX NUMBERS via 2 32.W 16.BP FIXED POINT NUMBERS -- Currently working with binary values


class evenOdd extends Module {
  val io = IO(new Bundle {
    val stage_count = Input(UInt(2.W))
    val count_en = Input(UInt(1.W))
    val in = Input(SInt(32.W))
    val BF_begin = Output(UInt(1.W))
    val out0_e = Output(SInt(32.W))
    val out1_e = Output(SInt(32.W))
    val out2_e = Output(SInt(32.W))
    val out3_e = Output(SInt(32.W))
    val out0_o = Output(SInt(32.W))
    val out1_o = Output(SInt(32.W))
    val out2_o = Output(SInt(32.W))
    val out3_o = Output(SInt(32.W))
    val stage_complete = Input(UInt(1.W))
  })

  // inits

  io.out0_e := 0.S
  io.out1_e := 0.S
  io.out2_e := 0.S
  io.out3_e := 0.S
  
  io.out0_o := 0.S
  io.out1_o := 0.S
  io.out2_o := 0.S
  io.out3_o := 0.S
  
  io.BF_begin := 0.U
  
  val inc = Module(new addrInc())  
  //inc.io.count_en := 1.U // will come from control unit eventually
  inc.io.count_en := io.count_en
  inc.io.stage_complete := io.stage_complete
  val rdy = inc.io.rdy_forBF

  val eRF = Module(new regFile4x32())  // even reg file
  val oRF = Module(new regFile4x32())  // odd reg file
 
  
  eRF.io.rdaddr0 := 0.U
  eRF.io.rdaddr1 := 0.U
  eRF.io.rdaddr2 := 0.U
  eRF.io.rdaddr3 := 0.U
  
  oRF.io.rdaddr0 := 0.U
  oRF.io.rdaddr1 := 0.U
  oRF.io.rdaddr2 := 0.U
  oRF.io.rdaddr3 := 0.U

  io.out0_e := eRF.io.out0
  io.out1_e := eRF.io.out1
  io.out2_e := eRF.io.out2
  io.out3_e := eRF.io.out3
  
  io.out0_o := oRF.io.out0
  io.out1_o := oRF.io.out1
  io.out2_o := oRF.io.out2
  io.out3_o := oRF.io.out3

  eRF.io.out_en := inc.io.rdy_forBF		// even output enable assigned to "rdy" signal from incrementer which is 111 condition
  oRF.io.out_en := inc.io.rdy_forBF		// odd " " 
  
  eRF.io.wraddr := inc.io.evenOdd_wraddr // connecting wraddr from incrementer to even/odd regfiles
  oRF.io.wraddr := inc.io.evenOdd_wraddr

  eRF.io.in := io.in
  oRF.io.in := io.in 

  eRF.io.wr_en := ~inc.io.evenOdd_wr_en  // writes to even reg file when LSB is 0
  oRF.io.wr_en := inc.io.evenOdd_wr_en  // writes to odd reg file when LSB is 1

/* In stage one, eo output is 00, 10
   In stage two, eo output is 00, 01
   In stage three, eo output is 00, 10 again
   Maybe have to deal with not conditions? elsewhen? else?
*/ 
// GETTING BONED BY THE CLOCK IN STAGE 0 --- FIGURE OUT A WAY TO LOAD ALL VALUES FIRST, THEN DUMP, MAYBE CONTROL UNIT MEMES
// ONCE ALL VALUES ARE STORED ITS OUTPUTTING THEM CORRECTLY. THE LAST OUTPUT OF THE ODD REG OUTPUTS BEFORE THE 7 GETS STRORED
// ITS BECAUSE 3'b111 HAPPENS AND IT DUMPS BEFORE THE 7 CAN ENTER THE MIX

// MIGHT BE FIXED? Though still outputting on 7th it, want it on the 8th it -- Maybe design actual FSM for the incrementer

  when (rdy.asBool&io.count_en.asBool){
	switch(io.stage_count){
		is(0.U){ // stage 0
			eRF.io.rdaddr0 := 0.U(2.W) // a input to 1st even BF
			eRF.io.rdaddr1 := 2.U(2.W) // b input to 1st even BF
			
			eRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd even BF
			eRF.io.rdaddr3 := 3.U(2.W) // b input to 2nd even BF
			
			
			oRF.io.rdaddr0 := 0.U(2.W) // a input to 1st odd BF
			oRF.io.rdaddr1 := 2.U(2.W) // b input to 1st odd BF
			
			oRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd odd BF
			oRF.io.rdaddr3 := 3.U(2.W) // b in put to 2nd odd BF
			
		}
		is(1.U){
			eRF.io.rdaddr0 := 0.U(2.W) // a input to 1st even BF
			eRF.io.rdaddr1 := 1.U(2.W) // b input to 1st even BF
			
			eRF.io.rdaddr2 := 2.U(2.W) // a input to 2nd even BF
			eRF.io.rdaddr3 := 3.U(2.W) // b input to 2nd even BF
			
			
			oRF.io.rdaddr0 := 0.U(2.W) // a input to 1st odd BF
			oRF.io.rdaddr1 := 1.U(2.W) // b input to 1st odd BF
			
			oRF.io.rdaddr2 := 2.U(2.W) // a input to 2nd odd BF
			oRF.io.rdaddr3 := 3.U(2.W) // b in put to 2nd odd BF
		}
		is(2.U){
			eRF.io.rdaddr0 := 0.U(2.W) // a input to 1st even BF
			eRF.io.rdaddr1 := 2.U(2.W) // b input to 1st even BF
			
			eRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd even BF
			eRF.io.rdaddr3 := 3.U(2.W) // b input to 2nd even BF
			
			
			oRF.io.rdaddr0 := 0.U(2.W) // a input to 1st odd BF
			oRF.io.rdaddr1 := 2.U(2.W) // b input to 1st odd BF
			
			oRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd odd BF
			oRF.io.rdaddr3 := 3.U(2.W) // b in put to 2nd odd BF
		}
		is(3.U){
			oRF.io.out_en := 0.U
			eRF.io.out_en := 0.U
			eRF.io.rdaddr0 := 0.U(2.W) // a input to 1st even BF
			eRF.io.rdaddr1 := 2.U(2.W) // b input to 1st even BF
			
			eRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd even BF
			eRF.io.rdaddr3 := 3.U(2.W) // b input to 2nd even BF
			
			
			oRF.io.rdaddr0 := 0.U(2.W) // a input to 1st odd BF
			oRF.io.rdaddr1 := 2.U(2.W) // b input to 1st odd BF
			
			oRF.io.rdaddr2 := 1.U(2.W) // a input to 2nd odd BF
			oRF.io.rdaddr3 := 3.U(2.W) // b in put to 2nd odd BF
		}
	}
	oRF.io.out_en := 1.U
        eRF.io.out_en := 1.U
  }	
	 
   io.BF_begin := inc.io.rdy_forBF
}

/*object EOMain extends App {
  println("Generating the incrementer hardware")
  (new chisel3.stage.ChiselStage).emitVerilog(new evenOdd(), Array("--target-dir", "generated"))

}*/
