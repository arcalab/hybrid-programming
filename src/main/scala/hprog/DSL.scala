package hprog

import hprog.ast.SymbolicExpr.SyExprAll
import hprog.ast._
import hprog.common.ParserException
import hprog.frontend.CommonTypes.Valuation
import hprog.frontend.solver.LiveSageSolver
import hprog.frontend.{Deviator, Distance, CommonTypes, Traj}
import hprog.lang.{Parser, SageParser}

import scala.language.implicitConversions

/**
 * Created by jose on 17/07/18.
 */
object DSL {
  implicit def str2Var(s:String): Var = ast.Var(s)
  implicit def bool2Cond(b:Boolean): BVal = BVal(b)
  implicit def real2Lin(n:Double): Value = Value(n)
  implicit def int2Lin(n:Int): Value = Value(n)
  implicit def real2Dur(n:Double): Dur = For(Value(n))
  implicit def int2Dur(n:Int): Dur = For(Value(n))
  implicit def cond2Dur(c:Cond): Dur = Until(c,None,None)
  implicit def dEq2dEqs(de:DiffEq): DiffEqs= DiffEqs(List(de),Forever)
  implicit def assg2Atom(a:Assign): Atomic = Atomic(List(a),DiffEqs(Nil,For(Value(0))))
  implicit def dEqs2Atom(des:DiffEqs): Atomic = Atomic(Nil,des)


  val x:Var="x"; val y:Var="y"; val p:Var="p"; val v:Var="v"; val g:Var="g"

  // examples
  val ex1 = Assign(x,Value(2))            // assignment
  val ex2 = (y^=3) & 34     // statement
  val ex3 = (x^=2) & (y^=3) // statement
  //val ex4 = (x^=2) ~ (y^=3) // program
  val ex5 = (y:=0) ~ (x:=0) ~ ((x^=2) & (y^=3) & 34) ~ ((x^=2) & (x > 2)) // program
  val ex6 = (x:=0) ~ ((x^=1) & ((x > 3*x) && (x<5)))
  val ex7 = ((p^=v) & (v^=g) & ((p<=0) && (v<=0))) ~ (v := (-0.5)*v) // bouncing ball
  val ex8 = (x^=1) & (((x > 3*x) && (x<5)) || (y>=3))
  val ex9 = (x^=1) & ((x > 3*x) && ((x<5) || (y>=3)))

  /**
    * Parses a string into a program.
    * @param s string representing a program
    * @return parsed program
    */
  def parse(s:String): Syntax =  {
    //println("parsing...")
    Parser.parse(s) match {
      case Parser.Success(result, _) =>
        //println("parsed")
        result
      case f: Parser.NoSuccess =>
        //println("failed")
        throw new ParserException(f.toString)
    }
  }

  /**
    * Parses a string into an expression.
    * @param s string representing an expression
    * @return parsed expression
    */
  def parseExpr(s:String): SyExprAll =  {
    //println("parsing...")
    SageParser.parseExpr(s) match {
      case SageParser.Success(result, _) =>
        //println("parsed")
        result
      case f: SageParser.NoSuccess =>
        //println("failed")
        throw new ParserException(f.toString)
    }
  }

  /**
    * Parses a string into an expression.
    * @param s string representing an expression
    * @return parsed expression
    */
  def parseCond(s:String): Cond =  {
    //println("parsing...")
    Parser.parseCond(s) match {
      case Parser.Success(result, _) =>
        //println("parsed")
        result
      case f: Parser.NoSuccess =>
        //println("failed")
        throw new ParserException(f.toString)
    }
  }

  val parseWithError: String => Parser.ParseResult[Syntax] = Parser.parse

//  def parseTraj(s:String,sagePath:String): Traj[Valuation] = // e.g., sagePath = "/home/jose/Applications/SageMath"
//    Semantics.syntaxToValuation(parse(s),new LiveSageSolver(sagePath), new Distance(10)).traj(Map())

}
