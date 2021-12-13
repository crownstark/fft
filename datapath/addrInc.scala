package fft

import chisel3._
import chisel3.util._


class addrInc extends Module {
  val io = IO(new Bundle {
    val mainReg_rdaddr = Output(UInt(3.W))
    val evenOdd_wraddr = Output(UInt(2.W))
    val evenOdd_wr_en = Output(UInt(1.W)) // controls whether EVEN or ODD gets written too -- tied to LSb of incrementer
    val rdy_forBF = Output(UInt(1.W))
    val count_en = Input(UInt(1.W))
    val stage_complete = Input(UInt(1.W))
  })

  io.mainReg_rdaddr := 0.U
  io.evenOdd_wraddr := 0.U
  io.evenOdd_wr_en := 0.U
  io.rdy_forBF := 0.U
  

    
  val reg = RegInit(0.U(3.W))
  when(io.count_en.asBool){
      io.mainReg_rdaddr := reg
      
      io.evenOdd_wraddr := reg >> 1
      io.evenOdd_wr_en := reg(0)
      io.rdy_forBF := reg(2)&reg(1)&reg(0) // connect this to control unit, and turn count_en LOW when this is HIGH
      when(~io.rdy_forBF.asBool & io.count_en.asBool){
        reg := reg + 1.U
      }
  }
      
      
}

/*object incMain extends App {
  println("Generating the incrementer hardware")
  (new chisel3.stage.ChiselStage).emitVerilog(new addrInc(), Array("--target-dir", "generated"))

}*/
