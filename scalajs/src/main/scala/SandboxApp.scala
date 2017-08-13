import org.scalajs.jquery.jQuery

import scala.scalajs.js.JSApp

/**
  * @author kasonchan
  * @since Aug-2017
  */
object SandboxApp extends JSApp {

  def main(): Unit = {
    jQuery(setupUI _)
  }

  def setupUI(): Unit = {
    jQuery("""<button type="button">What's up?</button>""")
      .click(addClickedMessage _)
      .appendTo(jQuery("body"))
    jQuery("body").append("<h1>Merry Christmas!</h1>")
  }

  def addClickedMessage(): Unit = {
    jQuery("body").append("<h1>Happy New Year!</h1>")
  }

}
