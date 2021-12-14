package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class BF_blocks_FP_TEST(dut: BF_blocks_FP) extends PeekPokeTester(dut){
 // TODO: TEST WHEN FP BF BLOCKS WORKING
 for (i <- 0 until 8){
   pokeFixedPoint(dut.io.in(i), i+1.1)
 }
 
 for (i <- 0 until 3){
   poke(dut.io.stage_counter, i.U)
   println("STAGE: " + i.toString)
   step(1)
   poke(dut.io.out_en, 1.U)
   for(j <- 0 until 8){
     println("out(" + j.toString + "): " + peekFixedPoint(dut.io.out(j)).toString)
    }
  }
}

object BF_blocks_FP_TEST extends App{
  println("Testing FP BF Blocks...")
  iotesters.Driver.execute(Array[String](), () => new BF_blocks_FP()){
    c => new BF_blocks_FP_TEST(c)
  }
}
