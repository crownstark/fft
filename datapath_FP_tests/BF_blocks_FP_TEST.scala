package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class BF_blocks_FP_TEST(dut: BF_blocks_FP) extends PeekPokeTester(dut){
 // TODO: TEST WHEN FP BF BLOCKS WORKING

}

object BF_blocks_FP_TEST extends App{
  println("Testing FP BF Blocks...")
  iotesters.Driver.execute(Array[String](), () => new BF_blocks_FP()){
    c => new BF_blocks_FP(c)
  }
}
