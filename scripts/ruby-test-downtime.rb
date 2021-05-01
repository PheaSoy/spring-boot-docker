# nethttp.rb
require 'uri'
require 'net/http'

uri = URI('http://localhost:8080/greeting;')
begin
    res = Net::HTTP.get_response(uri)
    puts res.code
    if res.is_a?(Net::HTTPSuccess)
        puts "OK!"
    else
        puts "<======Not OK!===>"
    end
    puts "Response ==> #{res.body}" if res.is_a?(Net::HTTPSuccess)
rescue => e
    puts "Error !!!!! #{e}"
end