package fft

import chisel3._
import chisel3.util._

// TODO : CHANGE TO SUPPORT COMPLEX NUMBERS via 2 32.W 16.BP FIXED POINT NUMBERS -- Currently working with binary values


class evenOdd extends Module {
  val io = IO(new Bundle {
    val stage_count = Input(UInt(2.W))
    val in = Input(UInt(32.W))
    val out0_e = Output(UInt(32.W))
    val out1_e = Output(UInt(32.W))
    val out0_o = Output(UInt(32.W))
    val out1_o = Output(UInt(32.W))
  })

  io.out0_e := 0.U
  io.out1_e := 0.U
  io.out0_o := 0.U
  io.out1_o := 0.U
  

  val inc = Module(new addrInc())  
  val rdy = inc.io.rdy

  val eRF = Module(new regFile4x32())  // even reg file
  val oRF = Module(new regFile4x32())  // odd reg file

  io.out0_e := eRF.io.out0
  io.out1_e := eRF.io.out1
  io.out0_o := oRF.io.out0
  io.out1_o := oRF.io.out1
  
  eRF.io.rdaddr0 := 0.U
  eRF.io.rdaddr1 := 0.U
  oRF.io.rdaddr0 := 0.U
  oRF.io.rdaddr1 := 0.U

  eRF.io.out_en := inc.io.rdy		// even output enable assigned to "rdy" signal from incrementer which is 111 condition
  oRF.io.out_en := inc.io.rdy		// odd " " 
  
  eRF.io.wraddr := inc.io.rd_shift // connecting wraddr from incrementer to even/odd regfiles
  oRF.io.wraddr := inc.io.rd_shift

  eRF.io.in := io.in
  oRF.io.in := io.in 

  eRF.io.wr_en := ~inc.io.rd_from  // writes to even reg file when LSB is 0
  oRF.io.wr_en := inc.io.rd_from  // writes to odd reg file when LSB is 1

/* In stage one, eo output is 00, 10
   In stage two, eo output is 00, 01
   In stage three, eo output is 00, 10 again
   Maybe have to deal with not conditions? elsewhen? else?
*/ 

  when (rdy.asBool){
	switch(io.stage_count){
		is(0.U){
			eRF.io.rdaddr0 := 0.U(2.W)
			eRF.io.rdaddr1 := 2.U(2.W)
			oRF.io.rdaddr0 := 0.U(2.W)
			oRF.io.rdaddr1 := 2.U(2.W)
		}
		is(1.U){
			eRF.io.rdaddr0 := 0.U(2.W)
			eRF.io.rdaddr1 := 1.U(2.W)
			oRF.io.rdaddr0 := 0.U(2.W)
			oRF.io.rdaddr1 := 1.U(2.W)
		}
		is(2.U){
			eRF.io.rdaddr0 := 0.U(2.W)
                        eRF.io.rdaddr1 := 2.U(2.W)
                        oRF.io.rdaddr0 := 0.U(2.W)
                        oRF.io.rdaddr1 := 2.U(2.W)
		}
		is(3.U){
			eRF.io.rdaddr0 := 0.U(2.W)
			eRF.io.rdaddr1 := 2.U(2.W)
			oRF.io.rdaddr0 := 0.U(2.W)
			oRF.io.rdaddr1 := 2.U(2.W)
		}
			
	 }
	 oRF.io.out_en := 1.U
         eRF.io.out_en := 1.U
     } .otherwise {
		oRF.io.out_en := 0.U
		eRF.io.out_en := 0.U
		eRF.io.rdaddr0 := 0.U(2.W)
		eRF.io.rdaddr1 := 2.U(2.W)
		oRF.io.rdaddr0 := 0.U(2.W)
		oRF.io.rdaddr1 := 2.U(2.W)
       }
    
}

//object EOMain extends App {
//  println("Generating the incrementer hardware")
//  (new chisel3.stage.ChiselStage).emitVerilog(new evenOdd(), Array("--target-dir", "generated"))

//}

