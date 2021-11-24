package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class evenOddTester(dut: evenOdd) extends PeekPokeTester(dut){
	// for stage 0 -- should output 0 and 4 from even and 1 and 5 from odd at last it -- WORKING
	poke(dut.io.stage_count, 0.U)
	println("STAGE 0 TEST")
	for (i <- 0 to 7){
		poke(dut.io.in, i.U)
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		step(1)
	}
	
	// for stage 1 -- should output 0 and 1 from even and 0 and 1 from odd -- WORKING
	poke(dut.io.stage_count, 1.U)
	println("STAGE 1 TEST")
	for (i <- 0 to 7){
		poke(dut.io.in, i.U)
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		step(1)
	}
	
	// for stage 2 -- should output 0 and 4 from even and 1 and 5 from odd again -- WORKING
	println("STAGE 2 TEST")
	poke(dut.io.stage_count, 2.U)
	for (i <- 0 to 7){
		poke(dut.io.in, i.U)
		println("Iteration: " + i.toString)
		println("out0 even: " + peek(dut.io.out0_e).toString)
		println("out1 even: " + peek(dut.io.out1_e).toString)
		println("out0 odd: " + peek(dut.io.out0_o).toString)
		println("out1 odd: " + peek(dut.io.out1_o).toString)
		step(1)
	}
              
}

object evenOddTester extends App {
	println("Testing evenOdd")
	iotesters.Driver.execute(Array[String](), () => new evenOdd()) {
		c => new evenOddTester(c)
	}
}
