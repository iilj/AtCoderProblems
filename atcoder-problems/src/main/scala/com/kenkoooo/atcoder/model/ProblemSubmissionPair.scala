package com.kenkoooo.atcoder.model

import com.kenkoooo.atcoder.common.TypeAnnotations.{ProblemId, SubmissionId}
import com.kenkoooo.atcoder.db.SQLSelectSupport
import scalikejdbc.{SQLSyntax, _}

trait ProblemSubmissionPair {
  def problemId: ProblemId

  def submissionId: SubmissionId
}

trait ProblemSubmissionPairSupport[T <: ProblemSubmissionPair] extends SQLSelectSupport[T] {
  override def apply(resultName: scalikejdbc.ResultName[T])(rs: WrappedResultSet): T =
    apply(
      problemId = rs.string(resultName.problemId),
      submissionId = rs.long(resultName.submissionId)
    )

  def apply(problemId: ProblemId, submissionId: SubmissionId): T

  def extractComparingColumn
    : QuerySQLSyntaxProvider[SQLSyntaxSupport[Submission], Submission] => SQLSyntax
}

case class Shortest(problemId: ProblemId, submissionId: SubmissionId) extends ProblemSubmissionPair

object Shortest extends ProblemSubmissionPairSupport[Shortest] {
  override def definedTableName: String = "shortest"

  override def extractComparingColumn
    : QuerySQLSyntaxProvider[SQLSyntaxSupport[Submission], Submission] => SQLSyntax = _.length
}

case class Fastest(problemId: ProblemId, submissionId: SubmissionId) extends ProblemSubmissionPair

object Fastest extends ProblemSubmissionPairSupport[Fastest] {
  override protected def definedTableName = "fastest"

  override def extractComparingColumn
    : QuerySQLSyntaxProvider[SQLSyntaxSupport[Submission], Submission] => SQLSyntax =
    _.executionTime
}

case class First(problemId: ProblemId, submissionId: SubmissionId) extends ProblemSubmissionPair

object First extends ProblemSubmissionPairSupport[First] {
  override protected def definedTableName = "first"

  override def extractComparingColumn
    : QuerySQLSyntaxProvider[SQLSyntaxSupport[Submission], Submission] => SQLSyntax = _.id
}
