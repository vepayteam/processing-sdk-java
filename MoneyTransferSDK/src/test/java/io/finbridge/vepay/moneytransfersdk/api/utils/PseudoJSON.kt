package io.finbridge.vepay.moneytransfersdk.api.utils

internal const val CreatePaymentResponse = """
{
  "acsRedirect": {
    "status": "OK",
    "url": "https://some-access-control-server.com/3ds?payId=123",
    "method": "POST",
    "postParameters": {
      "pa_req": "K8QufpC0UQXOoYcNlfT857Tvu15Wli12WXyvCyTX8AKY2QyMUCk",
      "md": "YcNlfT857Tvu15Wli12WXyvCyTX8AKY2QyMUCkGMifQ==",
      "term_url": "https://api.vepay.online/pay/orderdone/12345"
    }
  },
  "card": {
    "cardNumber": "4111111111111111",
    "cardHolder": "Terentiev Mihail",
    "expires": "0122"
  }
}
"""
