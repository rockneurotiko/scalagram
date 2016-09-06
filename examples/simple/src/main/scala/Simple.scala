import io.rockneurotiko.scalagram._

import akka.actor.ActorSystem
import io.rockneurotiko.scalagram.Implicits._
import io.rockneurotiko.scalagram.types._
// import io.rockneurotiko.scalagrammacros.Macros._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

class SimpleBot(token: String) extends Api(token) with Awesome {

  on { update =>
    println(s"Update received: $update")
  }

  onText { message =>
    answer(message).text(s"You said: ${message.text.get}").end()
  }

  onRegex("""emacs""".r) { message =>
    answer(message).text("You know what's good man!").end()
  }

  onCommand("start") { message =>
    answer(message).text("Hi :)").end()
  }

  oneTime(onRegex("onetime!".r) { message =>
    answer(message).text("Only once?").end()
  })

  onInline { inline =>
    // fluent api needed for inline!
    if (inline.query == "") {
      val article1 = InlineArticle("article1", "Test 1", InlineText("*Just a test 1!*"))
      // val article1 = InlineQueryResultArticle("article1", "Test 1", InputTextMessageContent("*Just a test 1!*", Some("markdown")))
      answerInlineQuery(AnswerInlineQuery(inline.id, List(article1)))
    }
  }

  onInlineRegex(".+".r) { inline =>
    // fluent api needed for inline!
    val articles = inline.query.split(" ").map(x => InlineQueryResultArticle(x, x, InputTextMessageContent(x)))
    answerInlineQuery(AnswerInlineQuery(inline.id, articles.toList))
  }

  def run() = longPoll()
}

object Simple extends App {
  implicit val system: ActorSystem = ActorSystem("my-super-system")
  val bot = new SimpleBot(Api.tokenFromFile("bot1.token"))
  bot.DEBUG = false
  val bot2 = new SimpleBot(Api.tokenFromFile("bot2.token"))
  bot.DEBUG = false

  MultiBotLongPoll.run(bot, bot2)
}
