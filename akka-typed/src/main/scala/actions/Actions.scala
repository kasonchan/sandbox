package actions

/**
  * @author kasonchan
  * @since 2018-09
  */
trait Action
case object Create extends Action
case object Start extends Action
case object Stop extends Action
case object Wait extends Action
case object State extends Action
