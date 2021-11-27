package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester
/*
class EO_RegFileTester(dut: EO_RegFile) extends PeekPokeTester(dut){
  println("STAGE 0 TEST")
  poke(dut.io.stage_count, 0.U)
  

}

object EO_RegFileTester extends App {
  println("Testing EO_RegFile...")
  iotesters.Driver.execute(Array[String](), () => new EO_RegFile()){
    c => new EO_RegFileTester(c)
  }
}

*/
