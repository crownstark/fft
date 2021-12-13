package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class evenOddTester(dut: evenOdd) extends PeekPokeTester(dut){
	// for stage 0 -- should output 0 and 4 from even and 1 and 5 from odd at last it -- WORKING -- UPDATED to HAVE 4 OUTPUTS. WORKS
	poke(dut.io.stage_count, 0.U)
	println("STAGE 0 TEST")
	poke(dut.io.count_en, 1.U)
	for (i <- 0 until 9){
		
		poke(dut.io.in, i.S)
		if(i==8){
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out2 even: " + peek(dut.io.out2_e).toString)
		println("out3 even: " + peek(dut.io.out3_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		println("out2 odd: " + peek(dut.io.out2_o).toString)
		println("out3 odd: " + peek(dut.io.out3_o).toString)
		//println("odd wren: " + peek(dut.io.odd_wren).toString)
		//println("BF_BEGIN = " + peek(dut.io.BF_begin).toString)
		}
		step(1)
		if(i==7){
		  poke(dut.io.count_en, 0.U) // have to flip count_en LOW on the 7th iteration, then flip HIGH on the next iteration, otherwise stage 0 doesn't work
		}
		poke(dut.io.count_en, 1.U)
		
	}
	
		
	
	// for stage 1 -- should output 0 and 1 from even and 0 and 1 from odd -- WORKING
	poke(dut.io.stage_count, 1.U)
	println("STAGE 1 TEST")
	for (i <- 0 until 9){
		poke(dut.io.in, i.S)
		if(i==8){
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out2 even: " + peek(dut.io.out2_e).toString)
		println("out3 even: " + peek(dut.io.out3_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		println("out2 odd: " + peek(dut.io.out2_o).toString)
		println("out3 odd: " + peek(dut.io.out3_o).toString)
		//println("BF_BEGIN = " + peek(dut.io.BF_begin).toString)
		}
		step(1)
		if(i==7){
		  poke(dut.io.count_en, 0.U)
		}
		poke(dut.io.count_en, 1.U)
	}
	
	// for stage 2 -- should output 0 and 4 from even and 1 and 5 from odd again -- WORKING
	println("STAGE 2 TEST")
	poke(dut.io.stage_count, 2.U)
	for (i <- 0 until 9){
		poke(dut.io.in, i.S)
		if(i==8){
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out2 even: " + peek(dut.io.out2_e).toString)
		println("out3 even: " + peek(dut.io.out3_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		println("out2 odd: " + peek(dut.io.out2_o).toString)
		println("out3 odd: " + peek(dut.io.out3_o).toString)
		//println("BF_BEGIN = " + peek(dut.io.BF_begin).toString)
		}
		step(1)
		if(i==7){
		  poke(dut.io.count_en, 0.U)
		}
		poke(dut.io.count_en, 1.U)
	}
	
   
     
}

object evenOddTester extends App {
	println("Testing evenOdd")
	iotesters.Driver.execute(Array[String](), () => new evenOdd()) {
		c => new evenOddTester(c)
	}
}
