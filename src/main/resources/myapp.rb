#
# override github-services Service::App to prevent Sinatra from taking over.
#
class Service::App
  def self.service(svc_class)
  end
end

