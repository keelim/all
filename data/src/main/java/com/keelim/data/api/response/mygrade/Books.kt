package com.keelim.data.api.response.mygrade


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Books(
    @SerialName("items") val items: List<Book> = emptyList(),
    @SerialName("kind") val kind: String? = "", // books#volumes
    @SerialName("totalItems") val totalItems: Int // 4254
) {
    @Serializable
    data class Book(
        @SerialName("accessInfo") val accessInfo: AccessInfo?,
        @SerialName("etag") val etag: String?, // Ortj+MnPLgs
        @SerialName("id") val id: String?, // Xv2yEAAAQBAJ
        @SerialName("kind") val kind: String?, // books#volume
        @SerialName("saleInfo") val saleInfo: SaleInfo?,
        @SerialName("searchInfo") val searchInfo: SearchInfo?,
        @SerialName("selfLink") val selfLink: String?, // https://www.googleapis.com/books/v1/volumes/Xv2yEAAAQBAJ
        @SerialName("volumeInfo") val volumeInfo: VolumeInfo?
    ) {
        @Serializable
        data class AccessInfo(
            @SerialName("accessViewStatus") val accessViewStatus: String?, // SAMPLE
            @SerialName("country") val country: String?, // KR
            @SerialName("embeddable") val embeddable: Boolean?, // true
            @SerialName("epub") val epub: Epub?,
            @SerialName("pdf") val pdf: Pdf?,
            @SerialName("publicDomain") val publicDomain: Boolean?, // false
            @SerialName("quoteSharingAllowed") val quoteSharingAllowed: Boolean?, // false
            @SerialName("textToSpeechPermission") val textToSpeechPermission: String?, // ALLOWED
            @SerialName("viewability") val viewability: String?, // PARTIAL
            @SerialName("webReaderLink") val webReaderLink: String? // http://play.google.com/books/reader?id=Xv2yEAAAQBAJ&hl=&source=gbs_api
        ) {
            @Serializable
            data class Epub(
                @SerialName("acsTokenLink") val acsTokenLink: String?, // http://books.google.co.kr/books/download/%EB%8F%84%EC%BB%A4_%EA%B5%90%EA%B3%BC%EC%84%9C-sample-epub.acsm?id=RgyCEAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api
                @SerialName("isAvailable") val isAvailable: Boolean? // false
            )

            @Serializable
            data class Pdf(
                @SerialName("acsTokenLink") val acsTokenLink: String?, // http://books.google.co.kr/books/download/%EB%8F%84%EC%BB%A4_%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88_%EB%B9%8C%EB%93%9C%EC%97%85-sample-pdf.acsm?id=DX9lEAAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api
                @SerialName("isAvailable") val isAvailable: Boolean? // false
            )
        }

        @Serializable
        data class SaleInfo(
            @SerialName("buyLink") val buyLink: String?, // https://play.google.com/store/books/details?id=Xv2yEAAAQBAJ&rdid=book-Xv2yEAAAQBAJ&rdot=1&source=gbs_api
            @SerialName("country") val country: String?, // KR
            @SerialName("isEbook") val isEbook: Boolean?, // true
            @SerialName("listPrice") val listPrice: ListPrice?,
            @SerialName("offers") val offers: List<Offer?>?,
            @SerialName("retailPrice") val retailPrice: RetailPrice?,
            @SerialName("saleability") val saleability: String? // FOR_SALE
        ) {
            @Serializable
            data class ListPrice(
                @SerialName("amount") val amount: Int?, // 47600
                @SerialName("currencyCode") val currencyCode: String? // KRW
            )

            @Serializable
            data class Offer(
                @SerialName("finskyOfferType") val finskyOfferType: Int?, // 1
                @SerialName("listPrice") val listPrice: ListPrice?,
                @SerialName("retailPrice") val retailPrice: RetailPrice?
            ) {
                @Serializable
                data class ListPrice(
                    @SerialName("amountInMicros") val amountInMicros: Long?, // 47600000000
                    @SerialName("currencyCode") val currencyCode: String? // KRW
                )

                @Serializable
                data class RetailPrice(
                    @SerialName("amountInMicros") val amountInMicros: Long?, // 42840000000
                    @SerialName("currencyCode") val currencyCode: String? // KRW
                )
            }

            @Serializable
            data class RetailPrice(
                @SerialName("amount") val amount: Int?, // 42840
                @SerialName("currencyCode") val currencyCode: String? // KRW
            )
        }

        @Serializable
        data class SearchInfo(
            @SerialName("textSnippet") val textSnippet: String? // 젯팩 <b>컴포즈</b>, 안드로이드 스튜디오, 코틀린으로 안드로이드 앱 개발하기 닐 스미스 (Neil Smyth). CHAPTER. 1818. <b>컴포즈</b>. 개요. 앞에서 안드로이드 스튜디오를 설치하고 코틀린 프로그래밍 언어의 기본 사항을 살펴봤으므로, 이 제 젯팩 <b>컴포즈</b>에 관해&nbsp;...
        )

        @Serializable
        data class VolumeInfo(
            @SerialName("allowAnonLogging") val allowAnonLogging: Boolean?, // false
            @SerialName("authors") val authors: List<String?>?,
            @SerialName("canonicalVolumeLink") val canonicalVolumeLink: String?, // https://play.google.com/store/books/details?id=Xv2yEAAAQBAJ
            @SerialName("categories") val categories: List<String?>?,
            @SerialName("contentVersion") val contentVersion: String?, // preview-1.0.0
            @SerialName("description") val description: String?, // 젯팩 컴포즈, 안드로이드 스튜디오, 코틀린으로 안드로이드 앱 개발하기 이 책은 젯팩 컴포즈, 안드로이드 스튜디오, 코틀린 프로그래밍 언어를 사용해 신속하고 빠르게 안정적이고 쾌적하게 동작하는 안드로이드 네이티브 UI 빌드 방법을 소개한다. 이 책에서 다루는 주제들은 자세한 튜토리얼 방식의 실습과 함께 진행되며, 다운로드할 수 있는 샘플 소스 코드도 함께 제공된다.
            @SerialName("imageLinks") val imageLinks: ImageLinks?,
            @SerialName("industryIdentifiers") val industryIdentifiers: List<IndustryIdentifier?>?,
            @SerialName("infoLink") val infoLink: String?, // https://play.google.com/store/books/details?id=Xv2yEAAAQBAJ&source=gbs_api
            @SerialName("language") val language: String?, // ko
            @SerialName("maturityRating") val maturityRating: String?, // NOT_MATURE
            @SerialName("pageCount") val pageCount: Int?, // 595
            @SerialName("panelizationSummary") val panelizationSummary: PanelizationSummary?,
            @SerialName("previewLink") val previewLink: String?, // http://books.google.co.kr/books?id=Xv2yEAAAQBAJ&pg=PA154&dq=%EC%BB%B4%ED%8F%AC%EC%A6%88&hl=&cd=1&source=gbs_api
            @SerialName("printType") val printType: String?, // BOOK
            @SerialName("publishedDate") val publishedDate: String?, // 2023-03-10
            @SerialName("publisher") val publisher: String?, // 제이펍
            @SerialName("readingModes") val readingModes: ReadingModes?,
            @SerialName("subtitle") val subtitle: String?, // 젯팩 컴포즈, 안드로이드 스튜디오, 코틀린으로 안드로이드 앱 개발하기
            @SerialName("title") val title: String? // 핵심만 골라 배우는 젯팩 컴포즈
        ) {
            @Serializable
            data class ImageLinks(
                @SerialName("smallThumbnail") val smallThumbnail: String?, // http://books.google.com/books/content?id=Xv2yEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api
                @SerialName("thumbnail") val thumbnail: String? // http://books.google.com/books/content?id=Xv2yEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api
            )

            @Serializable
            data class IndustryIdentifier(
                @SerialName("identifier") val identifier: String?, // 9791192469904
                @SerialName("type") val type: String? // ISBN_13
            )

            @Serializable
            data class PanelizationSummary(
                @SerialName("containsEpubBubbles") val containsEpubBubbles: Boolean?, // false
                @SerialName("containsImageBubbles") val containsImageBubbles: Boolean? // false
            )

            @Serializable
            data class ReadingModes(
                @SerialName("image") val image: Boolean?, // true
                @SerialName("text") val text: Boolean? // false
            )
        }
    }
}
