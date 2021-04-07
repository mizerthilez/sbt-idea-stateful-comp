package com.solan.idea.injector

import org.jetbrains.plugins.scala.lang.psi.types._
import org.jetbrains.plugins.scala.lang.psi.api.statements.params.ScClassParameter
import org.jetbrains.plugins.scala.lang.psi.api.base.ScPrimaryConstructor
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.{ScClass, ScTypeDefinition}
import org.jetbrains.plugins.scala.lang.psi.impl.toplevel.typedef.SyntheticMembersInjector
import com.intellij.openapi.diagnostic.Logger

class StatefulComponentInjector extends SyntheticMembersInjector {
  
  override def injectFunctions(source: ScTypeDefinition): Seq[String] = {
    source match {
      case clazz: ScClass if clazz.findAnnotationNoAliases("com.solan.macros.StatefulComponent") != null =>

        val (s1, s2) = clazz.members.collectFirst {
            case ctor: ScPrimaryConstructor => ctor.parameters.toList
          }.get.collect {
            case field if field.isCaseClassVal && field.name.charAt(0)=='_' =>
              val n = field.name.drop(1)
              val tpe = field.getRealParameterType.getOrAny
              (Seq(s"def $n: $tpe = ???", s"def ${n}_=(s: $tpe): Unit = ???"), s"$n: $tpe = this.$n")
          }.unzip
        s"def update(${s2.mkString(",")}): Unit = ???" :: s1.flatten

      case _ => Seq.empty
    }
  }
}

object StatefulComponentInjector {
  val log = Logger.getInstance("#com.solan.idea.injector.StatefulComponentInjector")
}

