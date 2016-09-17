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

  def customReply(message: Message) {
    answer(message).text(s"Awesome reply man!").end()
  }

  onCommand("reply") { message =>
    answer(message).text(s"Reply this!").force().end()
      .foreach(eithSended => {
        eithSended.right.foreach(sended => {
          oneTime {
            onReply(message, sended)(customReply(_))
          }
        })
      })
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
      val article1 = InlineArticle("article1", "Test 1", InlineTextContent("*Just a test 1!*").parseMode("markdown"))
      val article2 = InlineArticle("article2", "Test 2", InlineContent("*Just a test 1!*").parseMode("markdown"))

      answerInlineQuery(AnswerInlineQuery(inline.id, List(article1, article2)))
    }
  }

  onInlineRegex(".+".r) { inline =>
    // fluent api needed for inline!
    val articles = inline.query.split(" ").map(x => InlineArticle(x, x, InlineTextContent(x)))
    answerInlineQuery(AnswerInlineQuery(inline.id, articles.toList))
  }

  def run() = longPoll()
}

object Simple extends App {
  implicit val system: ActorSystem = ActorSystem("my-super-system")
  val bot = new SimpleBot(Api.tokenFromFile("bot1.token"))
  val bot2 = new SimpleBot(Api.tokenFromFile("bot2.token"))

  MultiBotLongPoll.run(bot, bot2)
}
