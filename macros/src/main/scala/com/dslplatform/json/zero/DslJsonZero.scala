package com.dslplatform.json
package zero

import com.softwaremill.debug.DebugConsole
import scala.reflect.macros.blackbox.Context
import scala.util.Try

object DslJsonZero {
  def serialize(param: Any): String =
    macro DslJsonZeroMacros.serialize_impl
}

object DslJsonZeroMacros extends DebugConsole {
  def serialize_impl(c: Context)(param: c.Expr[Any]): c.Expr[String] = {
    import c.universe.{Try => _, _}

    lazy val tryEval = Try {
      c.eval(c.Expr[Any](c.untypecheck(param.tree)))
    }

    def trySerializeConstant(): Option[c.Expr[String]] =
      param.tree match {
        case Literal(Constant(ObjectSerializer(json))) =>
          Some(c.Expr(Literal(Constant(json))))
        case other =>
          tryEval.toOption flatMap {
            case ObjectSerializer(json) =>
              Some(c.Expr(Literal(Constant(json))))
            case _ =>
              None
          }
      }

    trySerializeConstant().getOrElse(
      sys.error("Could not serialize: " + param.tree)
    )
  }
}
